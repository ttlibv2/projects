package vn.conyeu.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.conyeu.common.domain.LogDetail;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class Forbidden extends BaseException {

    public Forbidden() {
        super(HttpStatus.FORBIDDEN);
    }

    public Forbidden(LogDetail detail) {
        super(detail);
    }
}