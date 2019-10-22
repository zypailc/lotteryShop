package com.didu.lotteryshop.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.http.HttpServletResponse;

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
//        http
//                //.csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                // 开始认证
//                .and()
//                .requestMatchers().antMatchers("/**")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/authorization/**").authenticated()
//                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http
                // 开始认证
                .antMatcher("/**").authorizeRequests()
                // 对静态文件和登陆页面放行
                .antMatchers("/v1/authorization/**").permitAll()
                // 其他请求需要认证登陆
                .anyRequest().authenticated()
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    }
//    @Bean
//    public HttpSessionSecurityContextRepository contextRepository() {
//        return new HttpSessionSecurityContextRepository();
//    }
}
