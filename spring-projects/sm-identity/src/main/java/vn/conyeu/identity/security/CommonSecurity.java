package vn.conyeu.identity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CommonSecurity extends SecurityAdapter{

    @Order(9999999) @Bean("basicSecurityFilterChain")
    protected SecurityFilterChain basicSecurityFilterChain(HttpSecurity http) throws Exception {
        applyDefaultSecurityFilterChain(http);
        http.authorizeHttpRequests(cfg -> {
            cfg.requestMatchers("/error/**").permitAll();
            cfg.anyRequest().authenticated();
        });
        return http.build();
    }

}