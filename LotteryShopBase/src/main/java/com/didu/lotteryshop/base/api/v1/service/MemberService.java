package com.didu.lotteryshop.base.api.v1.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.didu.lotteryshop.base.api.v1.service.form.impl.MemberServiceImp;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.entity.EsEthwallet;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.service.form.impl.EsEthwalletServiceImpl;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
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
public class MemberService extends BaseService {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Autowired
    private MemberServiceImp memberServiceImp;
    @Autowired
    private EsEthwalletServiceImpl esEthwalletService;

    /**
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

}
