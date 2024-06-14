package vn.conyeu.identity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vn.conyeu.common.context.AppContext;
import vn.conyeu.identity.service.JwtService;
import vn.conyeu.identity.service.PrincipalService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class IdentitySecurityConfigurer extends SecurityAdapter {
    private final PrincipalService userDetailsService;

    @Autowired
    public IdentitySecurityConfigurer(PrincipalService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        //provider.setAuthoritiesMapper();
        //provider.setUserDetailsPasswordService();
        return provider;
    }



    @Order(2)@Bean("identitySecurityFilterChain")
    public SecurityFilterChain filterChainImpl2(HttpSecurity http) throws Exception {
        applyDefaultSecurityFilterChain(http);

        http.securityMatcher("/accounts/**", "/auth/**");

        http.authorizeHttpRequests(cfg -> cfg
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/accounts/**").authenticated()
        );

        AuthenticationManager authenticationManager = AppContext.getBean(AuthenticationManager.class);
        JwtService jwtService = AppContext.getBean(JwtService.class);
        http.addFilter(new SignInFilter(authenticationManager, jwtService));

        http.exceptionHandling(cfg -> {
           cfg.authenticationEntryPoint(new SimpleAuthenticationEntryPoint());
           cfg.accessDeniedHandler(new SimpleAccessDeniedHandler());
        });

        http.logout(cfg -> cfg
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"));


        http.authorizeHttpRequests(cfg -> {
           cfg.requestMatchers("/auth/**").permitAll();
           cfg.requestMatchers("/accounts/**").authenticated();
        });

        //return super.filterChainImpl(http);
        return http.build();
    }

}