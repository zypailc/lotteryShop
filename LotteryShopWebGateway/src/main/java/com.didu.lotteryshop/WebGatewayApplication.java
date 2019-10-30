package com.didu.lotteryshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * WEB 页面网关项目启动类
 * @author CHJ
 * @date 2019-10-21
 */
@SpringBootApplication(scanBasePackages = "com.didu")
@EnableZuulProxy
@EnableHystrix
@EnableDiscoveryClient
@EnableOAuth2Client
@EnableResourceServer
//@EnableOAuth2Sso
public class WebGatewayApplication  {

    public static void main(String[] args) {
        SpringApplication.run(WebGatewayApplication.class, args);
    }

}
