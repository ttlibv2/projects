package vn.conyeu.ts.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.conyeu.identity.security.JwtAuthenticationFilter;
import vn.conyeu.identity.security.JwtUPAuthenticationFilter;
import vn.conyeu.identity.security.SimpleAccessDeniedHandler;
import vn.conyeu.identity.security.SimpleAuthenticationEntryPoint;
import vn.conyeu.identity.service.JwtService;

@Configuration
public class TsAccountSecurity {

   // @Bean
    public SecurityFilterChain tsAccountSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, JwtService jwtService) throws Exception {
        http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable);
        http.authenticationManager(authenticationManager);
        http.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilter(new TsFilter());
        //http.addFilter(new JwtUPAuthenticationFilter(authenticationManager, jwtService));
       // http.addFilterAfter(new JwtAuthenticationFilter(jwtService, userDetailsService), UsernamePasswordAuthenticationFilter.class);
//        http.exceptionHandling(cfg -> {
//            cfg.authenticationEntryPoint(new SimpleAuthenticationEntryPoint());
//            cfg.accessDeniedHandler(new SimpleAccessDeniedHandler());
//        });




//        http.authorizeHttpRequests(cfg -> {
//            cfg.requestMatchers("/auth/**").permitAll();
//            cfg.requestMatchers("/error/**").permitAll();
//            cfg.anyRequest().authenticated();
//        });

        return http.build();
    }
}