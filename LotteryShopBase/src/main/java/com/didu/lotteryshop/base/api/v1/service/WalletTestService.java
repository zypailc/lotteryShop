package com.didu.lotteryshop.base.api.v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class WalletTestService {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public String get(){
        String str = "{\"requestData\":\"i7xBt1//dZKU1loTAZQnLFthL2EqyAtCXSgLCdsKUg6+wLbn7uByyF0PgBa99Hi9ybGWxLgscF8M/cxsMop1Jw==\"}";
        System.out.println("调用到了"+oAuth2RestTemplate.getAccessToken());
        //User user = oAuth2RestTemplate.getForObject("http://user-service/auth/v1/me", User.class);
        String reStr =   oAuth2RestTemplate.getForObject("http://wallet-service/test/get",String.class);
        return reStr;
    }

    public String save(){
        String str = "{\"requestData\":\"i7xBt1//dZKU1loTAZQnLFthL2EqyAtCXSgLCdsKUg6+wLbn7uByyF0PgBa99Hi9ybGWxLgscF8M/cxsMop1Jw==\"}";
        System.out.println("调用到了");
        //String jsonString = "{\"id\":10,\"name\":\"test\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        HttpEntity<String> strEntity = new HttpEntity<String>(str,headers);
        String reStr = oAuth2RestTemplate.postForObject("http://wallet-service/test/save",strEntity,String.class);
        return reStr;
    }

    public String test(){
        String reStr =   oAuth2RestTemplate.getForObject("http://wallet-service/test/test?name=CHJJJJJ",String.class);
        return reStr;
    }
}
