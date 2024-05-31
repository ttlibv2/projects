package vn.conyeu.common.restapi;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.conyeu.common.domain.LogDetail;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.helper.ErrorHelper;

import java.util.Map;

@Controller
@RestControllerAdvice
public class ExceptionRest extends ResponseEntityExceptionHandler implements ErrorController {

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Map<String, Object> attrs = ErrorHelper.getAttrs(request);
        return LogDetail.fromRequest(attrs).createResponse();
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException ex) {
        return ex.getObject().createResponse();
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        LogDetail log = LogDetail.e400().logCode("e_400").message("Vui lòng nhập đầy đủ thông tin.");

        for (FieldError field : ex.getFieldErrors()) {
            log.arguments(field.getField(), field.getDefaultMessage());
        }

        for (ObjectError field : ex.getGlobalErrors()) {
            log.arguments(field.getCode(), field.getDefaultMessage());
        }

        return ResponseEntity.status(status).headers(headers).body(log);
    }


}