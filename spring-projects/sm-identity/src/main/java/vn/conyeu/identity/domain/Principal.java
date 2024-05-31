package vn.conyeu.identity.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class Principal implements UserDetails {
    private final Account account;

    public Principal(Account account) {
        this.account = account;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getAuthorities();
    }

    public String getPassword() {
        return account.getPassword();
    }

    public String getUsername() {
        return "uid::"+account.getId();
    }

    public boolean isAccountNonExpired() {
        return account.isNonExpired();
    }

    public boolean isAccountNonLocked() {
        return account.isNonLocked();
    }

    public boolean isCredentialsNonExpired() {
        return account.isCredentialsNonExpired();
    }

    public boolean isEnabled() {
        return account.isActive();
    }

    public Long getUserId() {
        return account.getId();
    }
}