package vn.conyeu.identity.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.DateHelper;
import vn.conyeu.commons.utils.Objects;

import java.util.Collection;

@Getter
public class Principal implements UserDetails {
    private final ObjectMap custom = new ObjectMap();
    /**
     * -- GETTER --
     *  Returns the account
     */
    @Getter
    private final Account account;
    private String sessionId;
    private AccountToken token;


    public Principal(Account account) {
        this.account = account;
    }

    public Principal(Account account, String sessionId) {
        this.account = account;
        this.sessionId = sessionId;
    }

    public void setSessionId(String sessionId) {
        Asserts.isNull(this.sessionId, "The sessionId has set");
        this.sessionId = sessionId;
    }
    public void setToken(AccountToken token) {
        this.token = token;
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