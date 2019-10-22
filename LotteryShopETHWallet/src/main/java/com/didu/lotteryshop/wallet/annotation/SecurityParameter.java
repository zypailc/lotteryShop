package com.didu.lotteryshop.wallet.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;


/**
 *  请求&相应加密
 * @author CHJ
 * @date 2019-09-27 9:40
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface SecurityParameter {

    /**
     * 入参是否解密，默认解密
     */
    boolean inDecode() default true;

    /**
     * 出参是否加密，默认加密
     */
    boolean outEncode() default true;
}
