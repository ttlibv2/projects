package vn.conyeu.common.helper;

import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import vn.conyeu.common.exception.BaseException;

public final class Validators {

    public static void throwValidate(SmartValidator validator, Object object) {
        Errors errors = validator.validateObject(object);
        if(errors.hasErrors()) {
            BaseException exp = BaseException.e400("miss_param");
            exp.detail("list", errors);
            throw exp;
        }

    }
}