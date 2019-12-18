package com.didu.lotteryshop.base.api.v1.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.base.service.BaseBaseService;
import com.didu.lotteryshop.base.service.MailService;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.*;
import com.didu.lotteryshop.common.service.form.impl.*;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.BigDecimalUtil;
import com.didu.lotteryshop.common.utils.CodeUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService extends BaseBaseService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private MemberServiceImpl memberServiceImp;
    @Autowired
    private EsEthwalletServiceImpl esEthwalletService;
    @Autowired
    private MailService mailService;
    @Autowired
    private EsLsbwalletServiceImpl esLsbwalletService;
    @Autowired
    private EsDlbwalletServiceImpl esDlbwalletService;
    @Autowired
    private SysTaskServiceImpl sysTaskService;
    @Autowired
    private SysConfigServiceImpl sysConfigService;

    /**
     * 绑定钱包
     * @param paymentCode
     * @param bAddress
     * @return
     */
    public ResultUtil bindWallet(String paymentCode, String bAddress){
        LoginUser loginUser = super.getLoginUser();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",loginUser.getId());
        map.put("paymentCode",paymentCode);
        map.put("bAddress",bAddress);
        String reStr = "";
        ResultUtil result = null;
        Map<String, Object> resultMap = null;
        String WallName = "";
        boolean b = false;
        try {
            reStr = oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/generate", super.getEncryptRequestHttpEntity(map), String.class);
            if (reStr == null || "".equals(reStr)) {
                return ResultUtil.errorJson("Wallet creation failed !");
            }
            result = super.getDecryptResponseToResultUtil(reStr); //解密
            //判斷是否成功
            if (result != null && result.getCode() != ResultUtil.SUCCESS_CODE) {
                return ResultUtil.errorJson("Wallet creation failed !");
            }
            resultMap = (Map<String, Object>) result.getExtend().get(ResultUtil.DATA_KEY);
            if (resultMap != null) {
                WallName = AesEncryptUtil.encrypt(resultMap.get("fileName") == null ? "":resultMap.get("fileName").toString(), Constants.KEY_THREE);//钱包文件加密
                paymentCode = AesEncryptUtil.encrypt(paymentCode, Constants.KEY_TOW);//加密支付密码
                Member member = new Member();
                member.setId(loginUser.getId());
                member.setPAddress(resultMap.get("address").toString());
                member.setBAddress(bAddress);
                member.setWalletName(WallName);
                member.setPaymentCode(paymentCode);
                b = memberServiceImp.updateMember(member);
                if(!b){
                    return ResultUtil.errorJson("Failed to initialize wallet!");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //初始化钱包
        EsEthwallet esEthwallet = esEthwalletService.initInsert(loginUser.getId());
        if (esEthwallet == null) {
            return ResultUtil.errorJson("Failed to initialize wallet!");
        }
        return ResultUtil.successJson("successfully !");
    }

    /**
     * 修改用戶信息
     * @param bAddress
     * @return
     */
    public ResultUtil modifyBAddress(String bAddress){
        LoginUser loginUser = getLoginUser();
        Member member = new Member();
        member.setId(loginUser.getId());
        member.setBAddress(bAddress);
        boolean b = memberServiceImp.updateMember(member);
        if(b){
            return ResultUtil.successJson("modify successfully !");
        }
        return ResultUtil.successJson("fail to modify !");
    }

    /**
     * 查找推广用户
     * @return
     */
    public List<Map<String,Object>> findGeneralizeMemberList(){
        LoginUser loginUser = getLoginUser();
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq("generalize_member_id",loginUser.getId());
        List<Map<String,Object>> list = memberServiceImp.selectMaps(wrapper);
        return  list;
    }

    /**
     * 注冊用戶
     * @param member
     * @return
     */
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
        member.setUpdateTime(new Date());
        member.setGeneralizeType(0);
        //判斷是否有上級推薦
        if(member.getGeneralizeMemberId() != null && !"".equals(member.getGeneralizeMemberId())){
            //查詢上級基本用戶信息
            Member memberUp = new Member();
            memberUp = memberServiceImp.selectById(member.getGeneralizeMemberId());
            if(memberUp != null){
                member.setGeneralizeMemberId(memberUp.getGeneralizeMemberId());
                member.setGeneralizeMemberIds(memberUp.getGeneralizeMemberId() == null || "".equals(memberUp.getGeneralizeMemberId()) ? "":memberUp.getGeneralizeMemberId() + "," + memberUp.getId());
                member.setGeneralizeMemberLevel(memberUp.getGeneralizeMemberLevel() == null ? 0:(memberUp.getGeneralizeMemberLevel()+1));
            }
        }
        String emailStr = "Your account number is : " + member.getEmail() + "<br/>　Your password is : "+ password;
        mailService.sendSimpleMail(member,"password",emailStr);
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
               b = sysTaskService.TaskRegister(member.getId());
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
        member.setUpdateTime(new Date());
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
            String emailStr = "Your account number is : " + member.getEmail() + "<br/>　Your password is : "+ password;
            mailService.sendSimpleMail(member, "new password", emailStr);
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

    /**
     * 查询用户总资产
     * @param exchangeRate
     * @return
     */
    public ResultUtil findWalletTotal(String exchangeRate){
        if(exchangeRate != null && "".equals(exchangeRate)){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        LoginUser loginUser = getLoginUser();
        EsEthwallet ethwallet = esEthwalletService.findByMemberId(loginUser.getId());// eth
        EsDlbwallet dlbwallet = esDlbwalletService.findByMemberId(loginUser.getId());// dlb
        EsLsbwallet lsbwallet = esLsbwalletService.findByMemberId(loginUser.getId());// lsb
        SysConfig sysConfig = sysConfigService.getSysConfig();
        BigDecimal exchangeRateBig = new BigDecimal(exchangeRate).stripTrailingZeros();
        map.put("eth", BigDecimalUtil.bigDecimalToPrecision(ethwallet.getTotal()));
        map.put("ethToUsd",BigDecimalUtil.bigDecimalToPrecision(exchangeRateBig.multiply(ethwallet.getTotal())));
        map.put("dlb",dlbwallet.getTotal().stripTrailingZeros());
        BigDecimal dlbToEtb = dlbwallet.getTotal().divide(sysConfig.getLsbToEth(),BigDecimalUtil.BIGDECIMAL_PRECISION,BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros();
        map.put("dlb",dlbwallet.getTotal().stripTrailingZeros());//代领币
        map.put("dlbToEtb",dlbToEtb);
        map.put("dlbToUsd",BigDecimalUtil.bigDecimalToPrecision(dlbToEtb.multiply(exchangeRateBig)));
        BigDecimal lsbToEth = lsbwallet.getTotal().divide(sysConfig.getLsbToEth(),BigDecimalUtil.BIGDECIMAL_PRECISION,BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros();
        map.put("lsb",lsbwallet.getTotal().stripTrailingZeros());//平台币
        map.put("lsbToEth",lsbToEth.stripTrailingZeros());
        map.put("lsbToUsd",BigDecimalUtil.bigDecimalToPrecision(lsbToEth.multiply(exchangeRateBig)));
        BigDecimal ethTotal = ethwallet.getTotal().add(dlbToEtb).add(lsbToEth);
        map.put("ethTotal",BigDecimalUtil.bigDecimalToPrecision(ethTotal));
        map.put("ethTotalToUsd",BigDecimalUtil.bigDecimalToPrecision(ethTotal.multiply(exchangeRateBig)));
        return ResultUtil.successJson(map);
    }

}
