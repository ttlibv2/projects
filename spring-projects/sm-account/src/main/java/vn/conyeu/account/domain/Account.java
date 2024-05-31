package vn.conyeu.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.conyeu.common.domain.LongIdDate;

import java.util.ArrayList;
import java.util.Collection;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"password"})
@AttributeOverride(name = "id", column = @Column(name="accountId"))
//@formatter:on
public class Account extends LongIdDate<Account> implements UserDetails {

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(length = 100)
    private String password;

    @ColumnDefault("1")
    private Boolean active;

    @ColumnDefault("0")
    private Boolean locked;

    @ColumnDefault("0")
    private Boolean expires;

    private SignupType signupType;

    public Account(Long entityId) {
        super(entityId);
    }

    @JsonProperty("user_id")
    public Long getId() {
        return super.getId();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    public String getUsername() {
        return "uid::"+getId().toString();
    }

    public boolean isAccountNonExpired() {
        return !expires;
    }

    public boolean isAccountNonLocked() {
        return !locked;
    }

    public boolean isCredentialsNonExpired() {
        return !expires;
    }

    public boolean isEnabled() {
        return active;
    }
}