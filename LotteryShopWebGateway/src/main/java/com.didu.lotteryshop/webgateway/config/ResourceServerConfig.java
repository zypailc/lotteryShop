package com.didu.lotteryshop.webgateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * 〈资源认证服务器〉
 *
 * @author Curise
 * @create 2018/12/13
 * @since 1.0.0
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**").authorizeRequests()
                //允许通过的请求
                .antMatchers("/web/auth**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/plugins/**").permitAll()
                .antMatchers("/styles/**").permitAll()
                .antMatchers("/lang/**").permitAll()
                .antMatchers("/eurekaserver/**").permitAll()
                .antMatchers("/eureka/**").permitAll()
                .antMatchers("/authservice/**").permitAll()
                .antMatchers("/apiauthorization/**").permitAll()
                .antMatchers("/manage/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/favicon**").permitAll()
                .anyRequest().authenticated()
                //.and().formLogin().loginPage("/login").permitAll()
                // .and().logout().permitAll()
                // .and().logout().logoutSuccessUrl("/").permitAll()
                .and().csrf().disable();
               // .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //自定义authenticationEntryPoint，实现如果没有访问权限直接跳转到登录页。
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }


}
