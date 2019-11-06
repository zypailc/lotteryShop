package com.didu.lotteryshop.common.base.service;

import com.didu.lotteryshop.common.base.BaseStat;
import com.didu.lotteryshop.common.config.Constants;
import com.didu.lotteryshop.common.utils.AesEncryptUtil;
import com.didu.lotteryshop.common.utils.ResultUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

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
    };

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
     * 解密请求参数处理并转成成对象
     * @param enStr 加密字符串
     * @return ResultUtil
     */
    protected ResultUtil getDecryptRequestToResultUtil(String enStr){
        ResultUtil resultUtil = null;
        if(StringUtils.isNotBlank(enStr)){
            String jsonStr = this.getDecryptRequest(enStr);
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            resultUtil =(ResultUtil)JSONObject.toBean(jsonObject,ResultUtil.class);
        }
        return resultUtil;
    };

}
