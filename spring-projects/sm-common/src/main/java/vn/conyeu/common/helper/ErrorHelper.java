package vn.conyeu.common.helper;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import vn.conyeu.common.domain.RequestAttr;
import vn.conyeu.commons.beans.ObjectMap;

import java.util.Map;

public class ErrorHelper {

    public static HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) return HttpStatus.INTERNAL_SERVER_ERROR;
        try {return HttpStatus.valueOf(statusCode);}
        catch (Exception ex) {return HttpStatus.INTERNAL_SERVER_ERROR;}
    }

    public static Map<String, Object> getAttrs(HttpServletRequest request) {
        return getAttrs(new ServletWebRequest(request), allOption());
    }

    public static Map<String, Object> getAttrs(WebRequest request, ErrorAttributeOptions options) {
        ErrorAttributes errorAttributes = new DefaultErrorAttributes();
        Map<String, Object> map = errorAttributes.getErrorAttributes(request, options);
        if(options.isIncluded(Include.STACK_TRACE) || options.isIncluded(Include.EXCEPTION)) {
            map.put("throwable", errorAttributes.getError(request));
        }
        return map;
    }

    public static ErrorAttributeOptions allOption() {
        return ErrorAttributeOptions.of(ErrorAttributeOptions.Include.values());
    }

    public static boolean getErrorsParameter(HttpServletRequest request) {
        return getBooleanParameter(request, "errors");
    }

    public static boolean getBooleanParameter(HttpServletRequest request, String parameterName) {
        String parameter = request.getParameter(parameterName);
        if (parameter == null) return false;
        return !"false".equalsIgnoreCase(parameter);
    }















}