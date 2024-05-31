package vn.conyeu.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.conyeu.common.domain.LogDetail;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFound extends BaseException {

    public NotFound() {
        this("e_404");
    }

    public NotFound(String code) {
        super(HttpStatus.NOT_FOUND);
        code(code);
    }

    public NotFound(LogDetail detail) {
        super(detail);
    }
}