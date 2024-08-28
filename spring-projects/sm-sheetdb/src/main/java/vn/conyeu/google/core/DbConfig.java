package vn.conyeu.google.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.sheetdb")
public class DbConfig {

    /**
     * Application name.
     */
    private String appName;

    /**
     * Directory to store authorization tokens for this application.
     */
    private String tokenDir;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTokenDir() {
        return tokenDir;
    }

    public void setTokenDir(String tokenDir) {
        this.tokenDir = tokenDir;
    }
}