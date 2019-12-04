package com.didu.lotteryshop.webgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wg")
@PropertySource(value = "webGatewayConfig.properties")
public class WebGatewayConfig {

    /**
     * 项目访问路径
     */
    private String projectContent;

    public String getProjectContent() {
        return projectContent;
    }

    public void setProjectContent(String projectContent) {
        this.projectContent = projectContent;
    }
}
