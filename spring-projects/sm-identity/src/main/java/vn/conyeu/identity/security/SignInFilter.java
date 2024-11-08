package vn.conyeu.identity.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.identity.domain.AuthToken;
import vn.conyeu.identity.domain.Principal;
import vn.conyeu.identity.dtocls.SignInDto;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.identity.service.TokenService;

import java.io.IOException;
import java.util.Collections;

@Component
public class SignInFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenService tokenService;
    private boolean postOnly = true;

    @Autowired
    public SignInFilter(AuthenticationManager authenticationManager, TokenService jwtService) {
        super(authenticationManager);
        this.tokenService = Asserts.notNull(jwtService);

        // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
        // In our case, we use "/auth". So, we need to override the defaults.
        setRequiresAuthenticationRequestMatcher(defaultAntUri());

        // fail
        setAuthenticationFailureHandler(new SimpleAuthenticationFailureHandler());
    }

    protected RequestMatcher defaultAntUri() {
        String authUri = tokenService.getConfig().getAuthUri();
        return new AntPathRequestMatcher(authUri, "POST");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            // 1. Get credentials from request
            SignInDto dto = SignInDto.readRequest(request).validate();

            // 2. Create auth object (contains credentials) which will be used by auth manager
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    dto.getUser(), dto.getDecodePassword(), Collections.emptyList());

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
        Principal principal = (Principal) authResult.getPrincipal();

        String sessionId = tokenService.createAndSaveSessionId(principal.getUserId());
        principal.setSessionId(sessionId);

        AuthToken authToken = tokenService.buildToken(principal);
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