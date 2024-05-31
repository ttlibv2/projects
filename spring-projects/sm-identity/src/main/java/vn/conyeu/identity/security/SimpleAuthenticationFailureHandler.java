package vn.conyeu.identity.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import vn.conyeu.common.domain.LogDetail;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.helper.IdentityHelper;

import java.io.IOException;

public class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
       if(exception.getCause() instanceof BaseException exp) {
           ObjectMap logDetail = exp.getObject().createMapResponse();
           IdentityHelper.sendError(response, logDetail);
       }
       else {
           ObjectMap logDetail = LogDetail.e401().message(exception.getMessage()).throwable(exception).createMapResponse();
           IdentityHelper.sendError(response, logDetail);
       }
       // throw new UnsupportedEncodingException();
    }

}