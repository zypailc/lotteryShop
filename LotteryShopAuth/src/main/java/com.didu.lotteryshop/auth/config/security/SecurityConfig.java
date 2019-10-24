package com.didu.lotteryshop.auth.config.security;

import com.didu.lotteryshop.auth.service.impl.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 〈security配置〉
 *
 * @author Curise
 * @create 2018/12/13
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberServiceImpl memberService;
    //private MyUserDetailService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        return new NoEncryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //自定义登录押验证请求地址
        String loginProcessUrl="/custom/authorize";
        //自定义登录页面
        String loginPage="/custom/login";
        http.requestMatchers()
                .antMatchers("/oauth/**",loginPage,loginProcessUrl)
                .and().authorizeRequests()
                //密码模式需要允许“/auth/oauth/**”请求通过
                // .antMatchers("/auth/oauth/**",loginPage,loginProcessUrl)
                 .antMatchers(loginPage,loginProcessUrl)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
        // 表单登录
        http.formLogin()
                //  .failureHandler(handler)
                // 页面
                .loginPage(loginPage)
                // 登录处理url
                .loginProcessingUrl(loginProcessUrl);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

    /**
     * 不定义没有password grant_type,密码模式需要AuthenticationManager支持
     *MyUserDetailService
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
