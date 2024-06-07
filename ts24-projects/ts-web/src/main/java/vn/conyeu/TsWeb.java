package vn.conyeu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import vn.conyeu.identity.security.JwtConfig;

@EnableWebSecurity
@EnableMethodSecurity
@SpringBootApplication
//@EnableCaching
public class TsWeb {

    public static void main(String[] args) {

        SpringApplication.run(TsWeb.class, args);
    }

}