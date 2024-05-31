package vn.conyeu.account.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.conyeu.account.helper.AccountHelper;
import vn.conyeu.account.service.JwtService;
import vn.conyeu.account.service.PrincipalChecker;
import vn.conyeu.common.exception.BaseException;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserDetailsChecker userDetailsChecker;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public JwtAuthorizationFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userDetailsChecker = new PrincipalChecker();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken  = jwtService.resolveToken(request);
        if(accessToken != null) {
            try {
                String userName = jwtService.extractUsername(accessToken);

                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                // validate info + state user
                userDetailsChecker.check(userDetails);

                // update authentication to security
                AccountHelper.setAuthentication(userDetails);

            }
            catch (BaseException exp) {
                AccountHelper.sendError(response, exp.getObject().asMap());
                return;
            }

        }

        filterChain.doFilter(request, response);
    }
}