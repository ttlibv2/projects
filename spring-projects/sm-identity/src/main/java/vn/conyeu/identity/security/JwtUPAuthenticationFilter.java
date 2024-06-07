package vn.conyeu.identity.security;

import io.jsonwebtoken.JwtBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.identity.domain.AuthToken;
import vn.conyeu.identity.dtocls.SignInDto;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.identity.service.JwtService;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class JwtUPAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtService jwtService;
    private boolean postOnly = true;

    public JwtUPAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = Asserts.notNull(jwtService);

        // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
        // In our case, we use "/auth". So, we need to override the defaults.
        setRequiresAuthenticationRequestMatcher(defaultAntUri());

        // fail
        setAuthenticationFailureHandler(new SimpleAuthenticationFailureHandler());
    }

    protected RequestMatcher defaultAntUri() {
        String authUri = jwtService.getConfig().getAuthUri();
        return new AntPathRequestMatcher(authUri, "POST");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            // 1. Get credentials from request
            SignInDto dto = SignInDto.readRequest(request);

            // 2. Create auth object (contains credentials) which will be used by auth manager
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    dto.getUser(), dto.getPassword(), Collections.emptyList());

            // 3. Authentication manager authenticate the user, and use PrincipalService::loadUserByUsername() method to load the user.
            return getAuthenticationManager().authenticate(authToken);

        }//
        catch (IOException exp){
            throw new AuthenticationServiceException(exp.getMessage(), exp);
        }

    }

    // Upon successful authentication, generate a token.
    // The 'auth' passed to successfulAuthentication() is the current authenticated user.
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        JwtBuilder jwtBuilder = jwtService.generateToken( authResult.getName(), claims -> claims.add("authorities", authorities));
        AuthToken authToken = jwtService.buildToken(jwtBuilder);
        IdentityHelper.sendResponse(200, response, authToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        super.setPostOnly(postOnly);
        this.postOnly = postOnly;
    }
}