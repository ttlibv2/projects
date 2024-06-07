package vn.conyeu.identity.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

import java.util.Collection;

public class Principal implements UserDetails {
    private final ObjectMap custom = new ObjectMap();
    private final Account account;

    public Principal(Account account) {
        this.account = account;
        this.account.setInfo(null);
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

    /**
     * Returns the account
     */
    public Account getAccount() {
        return account;
    }

    public void set(String field, Object value) {
        if(value == null) custom.remove(field);
        else custom.set(field, value);
    }

    public <T> T get(Class<T> dataCls) {
        return get(dataCls, dataCls.getName());
    }

    public <T> T get(Class<T> dataCls, String field) {
        Object object = custom.get(field);
        if(dataCls.isInstance(object)) return dataCls.cast(object);
        else throw Objects.newIllegal("The value type `%s` not instance `%s`",
                object.getClass(), dataCls);
    }

    /**
     * Returns the custom
     */
    public ObjectMap custom() {
        return custom;
    }
}