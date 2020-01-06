package com.didu.lotteryshop.common.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
@Component
@ConfigurationProperties(prefix = "email")
@PropertySource(value = "emailConfig.properties")
public class MailCommonProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String protocol  = "smtp";
    private Charset defaultEncoding;
    private Map<String, String> properties ;
    private String jndiName;
    private boolean testConnection;


    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDefaultEncoding(Charset defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public void setTestConnection(boolean testConnection) {
        this.testConnection = testConnection;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProtocol() {
        return protocol;
    }

    public Charset getDefaultEncoding() {
        return defaultEncoding;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getJndiName() {
        return jndiName;
    }

    public boolean isTestConnection() {
        return testConnection;
    }
}
