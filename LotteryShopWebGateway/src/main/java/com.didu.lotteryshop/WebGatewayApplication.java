package com.didu.lotteryshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * WEB 页面网关项目启动类
 * @author CHJ
 * @date 2019-10-21
 */
@SpringBootApplication(scanBasePackages = "com.didu")
@EnableZuulProxy
@EnableHystrix
@EnableDiscoveryClient
@MapperScan("com.didu.lotteryshop.**.mapper*")
@EnableEurekaClient
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800,redisFlushMode = RedisFlushMode.IMMEDIATE)
public class WebGatewayApplication  {

    public static void main(String[] args) {
        SpringApplication.run(WebGatewayApplication.class, args);
    }


//    @Bean
//    public CookieSerializer httpSessionIdResolver() {
//        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//        // 取消仅限同一站点设置
//        //cookie名字
//       //cookieSerializer.setCookieName("sessionId");
//        //域
//        //cookieSerializer.setDomainName("xxx.com");
//       // cookieSerializer.setDomainName("127.0.0.1:8080");
//        //存储路径设为根路径，同一域名下多个项目共享该cookie
//        cookieSerializer.setCookiePath("/");
//        return cookieSerializer;
//    }
}
