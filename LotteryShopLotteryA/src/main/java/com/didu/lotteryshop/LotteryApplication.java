package com.didu.lotteryshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com")
@EnableOAuth2Client
@EnableResourceServer
@MapperScan("com.didu.lotteryshop.*.mapper*")
public class LotteryApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LotteryApplication.class);
    }
    @LoadBalanced
    @Bean
    public OAuth2RestTemplate loadBalancedRestTemplate(OAuth2ProtectedResourceDetails details, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(details, context);
    }
    public static void main(String [] args){
        SpringApplication.run(LotteryApplication.class);
    }
}
