package vn.conyeu.account.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import vn.conyeu.account.helper.AccountHelper;
import vn.conyeu.common.domain.LogDetail;
import vn.conyeu.commons.beans.ObjectMap;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class SimpleAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMap msgErr = ObjectMap.setNew("code", "e_401").set("message", authException.getMessage());
        AccountHelper.sendError(response, msgErr);
    }
}