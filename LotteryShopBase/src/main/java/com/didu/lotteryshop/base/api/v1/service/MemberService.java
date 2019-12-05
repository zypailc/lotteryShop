package com.didu.lotteryshop.base.api.v1.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.base.service.MailService;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.EsEthwallet;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.service.form.impl.*;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.CodeUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService extends BaseBaseService {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private MemberServiceImpl memberServiceImp;
    @Autowired
    private EsEthwalletServiceImpl esEthwalletService;
    @Autowired
    private MailService mailServiceImp;
    @Autowired
    private EsLsbwalletServiceImpl esLsbwalletService;
    @Autowired
    private EsDlbwalletServiceImpl esDlbwalletService;
    @Autowired
    private EsDlbaccountsServiceImpl esDlbaccountsService;
    @Autowired
    private SysConfigServiceImpl sysConfigService;

    /**
     * 绑定钱包
     * @param userId 用户Id
     * @param paymentCode
     * @param bAddress
     * @return
     */
    public ResultUtil bindWallet(String userId, String paymentCode, String bAddress){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",userId);
        map.put("paymentCode",paymentCode);
        map.put("bAddress",bAddress);
        String str = new JSONObject(map).toString();
        str = super.getEncryptRequest(str);//加密
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        HttpEntity<String> strEntity = new HttpEntity<String>(str,headers);
        String reStr =   oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/generate",strEntity,String.class);
        ResultUtil result = super.getDecryptRequestToResultUtil(reStr); //解密
        //保存
        if(result != null && result.getCode() == ResultUtil.ERROR_CODE){
            return ResultUtil.errorJson("error");
        }
        Member member = new Member();
        Map<String,Object> resultMap = (Map<String,Object>) result.getExtend().get(ResultUtil.DATA_KEY);
        member.setId(userId);
        member.setPAddress(resultMap.get("address").toString());
        member.setBAddress(bAddress);
        String WallName = "";
        try{
            WallName = AesEncryptUtil.encrypt(resultMap.get("fileName").toString(), Constants.KEY_THREE);//钱包文件加密
            paymentCode = AesEncryptUtil.encrypt(paymentCode, Constants.KEY_TOW);//加密支付密码
        }catch (Exception e){
            e.printStackTrace();
        }
        member.setWalletName(WallName);
        member.setPaymentCode(paymentCode);
        ResultUtil resultUtil = modifyMember(member);

        //初始化钱包
        EsEthwallet esEthwallet = esEthwalletService.initInsert(member.getId());
        if(esEthwallet == null){
            return ResultUtil.errorJson("error");
        }
        return resultUtil;
    }

    /**
     * 修改用户
     * @param member
     * @return
     */
    public ResultUtil modifyMember(Member member){
        member.setCreateTime(new Date());
        boolean b = memberServiceImp.updateById(member);
        if(b){
            return ResultUtil.successJson("success");
        }
        return ResultUtil.errorJson("error");
    }

    /**
     * 查找推广用户
     * @param userId
     * @return
     */
    public List<Map<String,Object>> findGeneralizeMemberList(String userId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq("generalize_member_id",userId);
        List<Map<String,Object>> list = memberServiceImp.selectMaps(wrapper);
        return  list;
    }

    public ResultUtil register(Member member) {
        //判断邮箱是否被注册
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq("email",member.getEmail());
        Member m = memberServiceImp.selectOne(wrapper);
        if(m != null && m.getEmail() != null){
            return ResultUtil.errorJson("The email address has been registered !");
        }
        //随机生成一个秘钥jsonObjectjsonObject
        String secretKey = Constants.KEY_TOW;
        //随机生成随机密码
        String password = CodeUtil.getCode(18);
        try {
            //秘钥和密码加密生成暗文
            String ciphertext = AesEncryptUtil.encrypt_code(password,secretKey);
            member.setPassword(ciphertext);
        }catch (Exception e){
            e.printStackTrace();
        }
        member.setId(CodeUtil.getUuid());
        member.setSecretKey(secretKey);
        member.setCreateTime(new Date());
        mailServiceImp.sendSimpleMail(member,"new password",password);
        //保存用户信息
        boolean b = memberServiceImp.insert(member);
        if(b){
            //生成平台币钱包
            b = esLsbwalletService.initInsert(member.getId()) != null;
            //生成待领币钱包
            if(b)
                b = esDlbwalletService.initInsert(member.getId()) != null;
            //注册送代领币
            if(b)
                b = esDlbaccountsService.addInSuccess(member.getId(),
                        EsDlbaccountsServiceImpl.DIC_TYPE_REGISTRATIONINCENTIVES,
                        sysConfigService.getSysConfig().getRegisterDlb(),
                        member.getId());
            if(b)
                return ResultUtil.successJson("Registered successfully , please log in !");
        }
        return ResultUtil.errorJson("Registered error , please operate again !");
    }

    /**
     * 重置密码
     * @param email
     * @return
     */
    public ResultUtil forgotPassword(String email){
        Member member = new Member();
        member.setEmail(email);
        //随机生成随机密码
        String password = CodeUtil.getCode(18);
        try {
            //秘钥和密码加密生成暗文
            String ciphertext = AesEncryptUtil.encrypt_code(password,Constants.KEY_TOW);
            member.setPassword(ciphertext);
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);
        Wrapper wrapper = new EntityWrapper<Member>();
        wrapper.allEq(map);
        boolean b = memberServiceImp.update(member,wrapper);
        if(b) {
            mailServiceImp.sendSimpleMail(member, "new password", password);
            return ResultUtil.successJson("Your new password has been sent to your email !");
        }
        return  ResultUtil.errorJson("error , please operate again !");
    }

    /**
     * 修改头像
     * @param member
     * @return
     */
    public ResultUtil headPortrait(Member member){
        boolean flag = memberServiceImp.updateById(member);
        if(flag) {
            return ResultUtil.successJson("modify successfully !");
        }else {
            return ResultUtil.successJson("fail to modify !");
        }
    }

}
