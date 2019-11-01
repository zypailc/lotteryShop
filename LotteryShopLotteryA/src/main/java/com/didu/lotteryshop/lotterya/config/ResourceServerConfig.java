package com.didu.lotteryshop.lotterya.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源认证服务器
 * @author CHJ
 * @date 2019-11-01
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 开始认证
                .antMatcher("/**").authorizeRequests()
                // 对静态文件和登陆页面放行
                .antMatchers("/authorization/**").permitAll()
                // 其他请求需要认证登陆
                .anyRequest().authenticated()
                .and().csrf();

    }
}
