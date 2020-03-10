package com.didu.lotteryshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.didu.lotteryshop.**.mapper*")
@EnableFeignClients
@EnableOAuth2Client
@EnableResourceServer
public class BaseApplication extends SpringBootServletInitializer  {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BaseApplication.class);
    }
    @LoadBalanced
    @Bean
    public OAuth2RestTemplate loadBalancedRestTemplate(OAuth2ProtectedResourceDetails details, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(details, context);
    }
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String [] args){
        SpringApplication.run(BaseApplication.class);
    }

}
