package com.didu.lotteryshop.common.base.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.didu.lotteryshop.common.base.BaseStat;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BaseService extends BaseStat {
    /**
     * 加密请求参数处理
     * @jsonStr jsonStr
     * @return 加密请求
     */
    protected String getEncryptRequest(String jsonStr){
        String eRStr = null;
        if(StringUtils.isNotBlank(jsonStr)){
            try {
               String eStr =  AesEncryptUtil.encrypt(jsonStr, Constants.AES_ETHWALLET_KEY);
               eRStr = "{\"requestData\":\""+eStr+"\"}";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return eRStr;
    }

    /**
     * 获取加密请求HttpEntity
     * @param map
     * @return
     */
    public HttpEntity<String> getEncryptRequestHttpEntity(Map<String,Object> map){
        String str = new org.springframework.boot.configurationprocessor.json.JSONObject(map).toString();
        str = this.getEncryptRequest(str);//加密
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        HttpEntity<String> strEntity = new HttpEntity<String>(str,headers);
        return strEntity;
    }

    /**
     * 解密请求参数处理
     * @param enStr 加密字符串
     * @return json字符串
     */
    protected String getDecryptRequest(String enStr){
        String deStr = null;
        if(StringUtils.isNotBlank(enStr)){
            try {
                 deStr =  AesEncryptUtil.decrypt(enStr, Constants.AES_ETHWALLET_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deStr;
    };
    /**
     * 解密响应参数处理并转成成对象
     * @param enStr 加密字符串
     * @return ResultUtil
     */
    protected ResultUtil getDecryptResponseToResultUtil(String enStr){
        ResultUtil resultUtil = null;
        if(StringUtils.isNotBlank(enStr)){
            //数据返回的数据可能有双引号，去除双引号
            enStr = enStr.replaceAll("\"","");
            String jsonStr = this.getDecryptRequest(enStr);
            resultUtil = this.getResponseToResultUtil(jsonStr);
        }
        return resultUtil;
    }

    /**
     * 请求响应参数处理并转换成对象
     * @param jsonStr
     * @return
     */
    protected ResultUtil  getResponseToResultUtil(String jsonStr){
        ResultUtil resultUtil = null;
        if(StringUtils.isNotBlank(jsonStr)){
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            resultUtil =(ResultUtil)JSONObject.toBean(jsonObject,ResultUtil.class);
            resultUtil.setExtend( (Map<String, Object>)JSONUtils.parse(jsonObject.getString("extend")));
        }
        return resultUtil;
    }


}
