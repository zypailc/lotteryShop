package com.didu.lotteryshop.base.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "qr")
@PropertySource(value = "baseConfig.properties")
public class BaseConfig {

    /**
     * 项目访问地址
     */
    private String url;

    /**
     * 二维码背景Id
     */
    private String QRBackgroundId;

    /**
     * 图片临时生成地址
     */
    private String imgTemporaryUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQRBackgroundId() {
        return QRBackgroundId;
    }

    public void setQRBackgroundId(String QRBackgroundId) {
        this.QRBackgroundId = QRBackgroundId;
    }

    public String getImgTemporaryUrl() {
        return imgTemporaryUrl;
    }

    public void setImgTemporaryUrl(String imgTemporaryUrl) {
        this.imgTemporaryUrl = imgTemporaryUrl;
    }
}
