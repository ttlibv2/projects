package vn.conyeu.identity.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.identity.service.JwtService;
import vn.conyeu.identity.service.PrincipalService;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final PrincipalService principalService;

    public JwtAuthenticationFilter(JwtService jwtService, PrincipalService principalService) {
        this.jwtService = jwtService;
        this.principalService = principalService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.resolveToken(request);
        if (accessToken != null) {
            try {
                String userName = jwtService.extractUsername(accessToken);
                UserDetails userDetails = principalService.loadUserByUsername(userName);

                // validate info + state user
                principalService.check(userDetails);

                // update authentication to security
                IdentityHelper.setAuthentication(userDetails);
            }//
            catch (JwtException exp) {
                String code = IdentityHelper.extractCode(exp);
                throw BaseException.e401(code)
                        .message(exp.getMessage())
                        .throwable(exp);
            }
        }

        filterChain.doFilter(request, response);
    }
}