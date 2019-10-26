package com.didu.lotteryshop.webgateway.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * 安全框架配置文件
 * @author zm
 * @date 2019-10-23
 */
@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
                .antMatchers("/lang/**").permitAll()
                .antMatchers("/eurekaserver/**").permitAll()
                .antMatchers("/eureka/**").permitAll()
                //.antMatchers("/authservice/**").permitAll()
                .antMatchers("/apiauthorization/**").permitAll()
                .antMatchers("/base/**").permitAll()
                .anyRequest().authenticated()
                //.and().formLogin().loginPage("/login").permitAll()
                // .and().logout().permitAll()
                // .and().logout().logoutSuccessUrl("/").permitAll()
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
