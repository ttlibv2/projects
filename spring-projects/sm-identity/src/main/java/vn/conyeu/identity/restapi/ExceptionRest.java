package vn.conyeu.identity.restapi;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.conyeu.common.domain.LogDetail;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.helper.ErrorHelper;
import vn.conyeu.identity.helper.IdentityHelper;

import java.util.Map;

@RestController
@RestControllerAdvice
public class ExceptionRest extends ResponseEntityExceptionHandler implements ErrorController {


    @RequestMapping("${server.error.path:${error.path:/error}}")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Map<String, Object> attrs = ErrorHelper.getAttrs(request);
        return LogDetail.fromRequest(attrs).createResponse();
    }

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<Object> handleBaseException(BaseException exp) {
        return exp.getObject().createResponse();
    }

    @ExceptionHandler({JwtException.class})
    protected ResponseEntity<Object> handleJwtException(JwtException exp) {
        return LogDetail.e404().throwable(exp)
                .logCode(IdentityHelper.extractCode(exp))
                .message(exp.getMessage()).createResponse();
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

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return LogDetail.e404().logCode("no_resource").message("Đường dẫn không tồn tại")
                .detail("path", ex.getResourcePath()).createResponse();
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ResponseEntity<Object> responseEntity = super.handleExceptionInternal(ex, body, headers, statusCode, request);
        return responseEntity;
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if(body instanceof ProblemDetail pd) {
            return LogDetail.eStatus(pd.getStatus()).message(pd.getDetail())
                    .detail("path", pd.getInstance()).createResponse();
        }
        return super.createResponseEntity(body, headers, statusCode, request);
    }
}