package vn.conyeu.account.domain;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class NotFoundUser extends UsernameNotFoundException {

    public NotFoundUser(String msg) {
        super(msg);
    }

}