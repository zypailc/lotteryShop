package com.didu.lotteryshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * WEB 页面网关项目启动类
 * @author CHJ
 * @date 2019-10-21
 */
@SpringBootApplication(scanBasePackages = "com.didu")
@EnableZuulProxy
@EnableHystrix
@EnableDiscoveryClient
@EnableOAuth2Sso
//@EnableOAuth2Client
public class WebGatewayApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(WebGatewayApplication.class, args);
    }

    /**
     * Security验证服务权限设置
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .antMatcher("/**").authorizeRequests()
                //允许通过的请求
                .antMatchers("/web/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/plugins/**").permitAll()
                .antMatchers("/styles/**").permitAll()
                .antMatchers("/eurekaserver/**").permitAll()
                .antMatchers("/eureka/**").permitAll()
                //.antMatchers("/authservice/**").permitAll()
                .antMatchers("/apiauthorization/**").permitAll()
                .anyRequest().authenticated()
                //.and().formLogin().loginPage("/login").permitAll()
               // .and().logout().permitAll()
               // .and().logout().logoutSuccessUrl("/").permitAll()
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
