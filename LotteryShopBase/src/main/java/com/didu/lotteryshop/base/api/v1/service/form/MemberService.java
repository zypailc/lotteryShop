package com.didu.lotteryshop.base.api.v1.service.form;

import cn.hutool.core.convert.Convert;
import com.didu.lotteryshop.base.api.v1.service.form.imp.MemberServiceImp;
import com.didu.lotteryshop.common.base.service.BaseService;
import com.didu.lotteryshop.common.entity.EsEthwallet;
import com.didu.lotteryshop.common.entity.Member;
import com.didu.lotteryshop.common.service.form.impl.EsEthwalletServiceImpl;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ConfigurationUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import net.sf.ezmorph.bean.MorphDynaBean;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.HashMap;
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
        paymentCode = AesEncryptUtil.encrypt_code(paymentCode,ConfigurationUtil.KEY_TOW);//加密支付密码
        /*MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("userId",userId);
        paramMap.add("paymentCode",paymentCode);*/
        //map.put("bAddress",bAddress);
        /*String str =  Convert.toStr(paramMap);
        str = super.getEncryptRequest(str);//加密
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        HttpEntity<String> strEntity = new HttpEntity<String>(str,headers);*/
        String reStr =   oAuth2RestTemplate.postForObject("http://wallet-service/v1/wallet/generate?userId={userId}&paymentCode={paymentCode}",null,String.class,map);
        ResultUtil result = super.getDecryptRequestToResultUtil(reStr); //解密
        //保存
        Member member = new Member();
        MorphDynaBean morphDynaBean = (MorphDynaBean) result.getExtend().get("data");
        member.setId(userId);
        member.setPaymentCode(paymentCode);
        member.setPAddress(morphDynaBean.get("address").toString());
        member.setBAddress(bAddress);
        String WallName = "";
        try{
            WallName = AesEncryptUtil.encrypt(morphDynaBean.get("fileName").toString(), ConfigurationUtil.KEY_THREE);
        }catch (Exception e){
            e.printStackTrace();
        }
        member.setWalletName(WallName);
        ResultUtil resultUtil = modifyMember(member);
        //初始化钱包
        EsEthwallet esEthwallet = esEthwalletService.initInsert(member.getId());
        if(esEthwallet == null){
            return ResultUtil.errorJson("error");
        }
        return resultUtil;
    }

    public ResultUtil modifyMember(Member member){
        member.setCreateTime(new Date());
        boolean b = memberServiceImp.updateById(member);
        if(b){
            return ResultUtil.successJson("success");
        }
        return ResultUtil.errorJson("error");
    }

}
