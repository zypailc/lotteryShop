package com.didu.lotteryshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 *  API网关项目启动类
 * @author  CHJ
 * @date 2019-10-21
 */
@SpringBootApplication(scanBasePackages = "com.didu")
@EnableZuulProxy
@EnableHystrix
@EnableResourceServer
@EnableEurekaClient
public class APIGatewayApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(APIGatewayApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(APIGatewayApplication.class, args);
    }
}
