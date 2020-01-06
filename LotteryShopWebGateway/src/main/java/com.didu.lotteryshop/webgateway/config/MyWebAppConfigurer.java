package com.didu.lotteryshop.webgateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new WebInterceptor()).addPathPatterns("/**");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //重写这个方法，映射静态资源文件
//        registry.addResourceHandler(
//      "/resources/**",
//                     "/static/**",
//                     "/images/**",
//                     "/js/**",
//                     "/lang/**",
//                     "/plugins/**",
//                     "/styles/**"
//                )
                registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/images/")
                .addResourceLocations("classpath:/js/")
                .addResourceLocations("classpath:/lang/")
                .addResourceLocations("classpath:/plugins/")
                .addResourceLocations("classpath:/styles/");
        super.addResourceHandlers(registry);
    }

}

