package vn.conyeu.identity.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.conyeu.common.context.AppContext;

import java.util.List;


public abstract class SecurityAdapter {

    //@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(false);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    public void applyDefaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //http.securityMatcher("/**");
        http.cors(this::applyCors);
        http.csrf(this::applyCsrf);
        http.httpBasic(this::applyHttpBasic);
        http.formLogin(this::applyFormLogin);
        http.sessionManagement(this::applySessionManagement);

        //addFilterAfter
        JwtAuthenticationFilter jwtAuthenticationFilter = AppContext.getBean(JwtAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


    /**
     * Allows configuring of Session Management.
     * @see HttpSecurity#sessionManagement(Customizer)
     * */
    protected void applySessionManagement(SessionManagementConfigurer<HttpSecurity> cfg) {
        cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    protected void applyFormLogin(FormLoginConfigurer<HttpSecurity> cfg) {
        cfg.disable();
    }

    protected void applyHttpBasic(HttpBasicConfigurer<HttpSecurity> cfg) {
        cfg.disable();
    }

    protected void applyCsrf(CsrfConfigurer<HttpSecurity> cfg) {
        cfg.disable();
    }

    protected void applyCors(CorsConfigurer<HttpSecurity> cfg) {
        cfg.configurationSource(corsConfigurationSource());
    }


}