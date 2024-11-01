package vn.conyeu.common.helper;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import vn.conyeu.common.exception.BaseException;

public final class Validators {

    public static void throwValidate(SmartValidator validator, Object object) {
        Errors errors = validator.validateObject(object);
        if(errors.hasErrors()) {
            BaseException exp = BaseException.e400("miss_param").message("Vui lòng nhập đầy đủ thông tin.");

            for (FieldError field : errors.getFieldErrors()) {
                exp.arguments(field.getField(), field.getDefaultMessage());
            }

            for (ObjectError field : errors.getGlobalErrors()) {
                exp.arguments(field.getCode(), field.getDefaultMessage());
            }

            exp.detail("list", errors);
            throw exp;
        }

    }
}