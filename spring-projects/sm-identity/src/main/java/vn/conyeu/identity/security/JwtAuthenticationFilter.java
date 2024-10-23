package vn.conyeu.identity.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.conyeu.common.domain.LogDetail;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.identity.service.PrincipalService;
import vn.conyeu.identity.service.TokenService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final PrincipalService principalService;

    public JwtAuthenticationFilter(TokenService tokenService, PrincipalService principalService) {
        this.tokenService = tokenService;
        this.principalService = principalService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenService.resolveToken(request);

        if (accessToken != null) {
            try {
                principalService.authenticationByToken(accessToken);
            }
            catch (JwtException exp) {
                SecurityContextHolder.clearContext();
                String code = IdentityHelper.extractCode(exp);
                IdentityHelper.sendError(response, LogDetail.e401(code)
                        .message(exp.getMessage()).throwable(exp)
                        .createMapResponse());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}