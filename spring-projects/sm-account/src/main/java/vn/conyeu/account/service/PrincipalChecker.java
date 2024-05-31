package vn.conyeu.account.service;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class PrincipalChecker implements UserDetailsChecker {

    public void check(UserDetails user) {
        MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

        if (!user.isAccountNonLocked()) {
            throw new LockedException(messages.getMessage("user.locked", "User account is locked"));
        }
        if (!user.isEnabled()) {
            throw new DisabledException(messages.getMessage("user.disabled", "User is disabled"));
        }
        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException(messages.getMessage("user.expired", "User account has expired"));
        }

        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(messages.getMessage("user.credentialsExpired", "User credentials have expired"));
        }

    }

}