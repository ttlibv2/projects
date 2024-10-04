package vn.conyeu.ts.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.conyeu.common.context.AppContext;
import vn.conyeu.identity.domain.Principal;

import java.io.IOException;

@Slf4j
public class TsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof Principal principal) {
//            OdAllService.AppForUser service = AppContext
//                    .getBean(OdAllService.class)
//                    .loadService(principal.getUserId());








//            OdService service = OdService.forUser(principal.getUserId());
//            service.setApiService(AppContext.getBean(UserApiService.class));
//            service.loadApi(OdTicketService.SERVICE_NAME);
//            principal.set(OdService.class.getName(), service);
        }
        filterChain.doFilter(request, response);
    }
}