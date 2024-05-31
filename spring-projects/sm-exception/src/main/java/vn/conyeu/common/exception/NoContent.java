package vn.conyeu.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.conyeu.common.domain.LogDetail;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContent extends BaseException {

    public NoContent() {
        super(HttpStatus.NO_CONTENT);
    }

    public NoContent(LogDetail detail) {
        super(detail);
    }


}