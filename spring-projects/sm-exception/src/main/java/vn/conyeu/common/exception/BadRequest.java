package vn.conyeu.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.conyeu.common.domain.LogDetail;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequest extends BaseException {

    public BadRequest() {
        this("e400");
    }

    public BadRequest(String code) {
        super(HttpStatus.BAD_REQUEST);
        code(code);
    }

    public BadRequest(LogDetail detail) {
        super(detail);
    }
}