package vn.conyeu.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.conyeu.common.domain.LogDetail;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class Unauthorized extends BaseException {

    public Unauthorized() {
        this("e_401");
    }

    public Unauthorized(String code) {
        super(HttpStatus.UNAUTHORIZED);
        code(code);
    }

    public Unauthorized(LogDetail detail) {
        super(detail);
    }

    @Override
    public Unauthorized message(String message) {
         super.message(message);
         return this;
    }

    @Override
    public Unauthorized message(String message, Object... arguments) {
        super.message(message, arguments);
        return this;
    }
}