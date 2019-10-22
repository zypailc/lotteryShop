package com.didu.lotteryshop.base.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 微服务之间的调用配置
 */
@Configuration
public class FeignConfig {

    //@Bean
   // public Contract feignContract(){
       // return new feign.Contract.Default();
   // }

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
