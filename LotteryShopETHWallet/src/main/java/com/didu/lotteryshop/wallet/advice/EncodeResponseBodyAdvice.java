package com.didu.lotteryshop.wallet.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.didu.lotteryshop.wallet.annotation.SecurityParameter;
import com.didu.lotteryshop.wallet.utils.AesEncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/***
 * 返回数据加密
 * @author CHJ
 * @date 2019-09-27 9:41
 */
@ControllerAdvice(basePackages = "com.didu.lotteryshop.wallet.contorller")
public class EncodeResponseBodyAdvice implements ResponseBodyAdvice {

    private final static Logger logger = LoggerFactory.getLogger(EncodeResponseBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        boolean encode = false;
        if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
            //获取注解配置的包含和去除字段
            SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
            //出参是否需要加密
            encode = serializedField.outEncode();
        }
        if (encode) {
            logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行加密");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                return AesEncryptUtil.encrypt(result);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常："+e.getMessage());
            }
        }
        return body;
    }
}
