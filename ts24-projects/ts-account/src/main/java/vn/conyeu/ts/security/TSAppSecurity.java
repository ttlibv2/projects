package vn.conyeu.ts.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.conyeu.identity.security.SecurityAdapter;
import vn.conyeu.ts.dtocls.TsVar;

@Configuration
public class TSAppSecurity extends SecurityAdapter {

    @Order(3)
    @Bean("tsAppSecurityFilterChain")
    public SecurityFilterChain filterChainImpl3(HttpSecurity http) throws Exception {
        applyDefaultSecurityFilterChain(http);

        http.securityMatcher(TsVar.Rest.tsApiPrefix + "/**", TsVar.Rest.odTicketApiPrefix + "/**");
        http.addFilterAfter(new TsFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(cfg -> cfg.anyRequest().authenticated());
        return http.build();
    }


}