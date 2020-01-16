package com.didu.lotteryshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 后台管理启动类
 * @author CHJ
 * @date 2019-11-05
 */
@SpringBootApplication(scanBasePackages = "com.didu")
@MapperScan("com.didu.lotteryshop.**.mapper*")
public class ManageApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ManageApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
    }
}
