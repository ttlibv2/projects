package vn.conyeu;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.ehcache.xml.model.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableMethodSecurity
@SpringBootApplication
public class TsWeb {

    public static void main(String[] args) {

        SpringApplication.run(TsWeb.class, args);
    }

}