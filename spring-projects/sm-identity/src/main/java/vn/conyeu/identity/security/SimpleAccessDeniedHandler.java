package vn.conyeu.identity.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import vn.conyeu.common.domain.LogDetail;
import vn.conyeu.identity.helper.IdentityHelper;

import java.io.IOException;

public class SimpleAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        LogDetail msgErr = LogDetail.e403().logCode("access_denied").message(exception.getMessage());
        IdentityHelper.sendError(response, msgErr.createMapResponse());
    }
}