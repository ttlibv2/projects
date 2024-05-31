package vn.conyeu.identity.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import vn.conyeu.common.domain.LogDetail;
import vn.conyeu.identity.helper.IdentityHelper;

import java.io.IOException;

public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LogDetail msgErr = LogDetail.e401().message(authException.getMessage());
        IdentityHelper.sendError(response, msgErr.createMapResponse());
    }

}