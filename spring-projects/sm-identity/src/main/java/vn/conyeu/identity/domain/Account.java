package vn.conyeu.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vn.conyeu.common.domain.LongIdDate;
import vn.conyeu.common.domain.StringId;
import vn.conyeu.common.domain.StringIdDate;
import vn.conyeu.identity.domain.enums.AccountStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//@formatter:off
@Entity @Table
@Getter() @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "accountId"))
//@formatter:on
public class Account extends LongIdDate<Account> {

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 100, nullable = false)
    private String password;

    @ColumnDefault("0")
    private Boolean expired;

    @ColumnDefault("0")
    private Boolean locked;

    @ColumnDefault("1")
    private Boolean active;

    @ColumnDefault("0")
    private Boolean credentialsExpired;

    @Enumerated(EnumType.STRING)
    private SignupType signupType;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")
    private AccountInfo info;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "AccountRole",
            joinColumns = @JoinColumn(name = "accountId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles;

    public Set<Role> getRoles() {
        if(roles == null) roles = new HashSet<>();
        return roles;
    }

    public boolean isNonExpired() {
        return expired == null || !expired;
    }

    public boolean isNonLocked() {
        return locked == null || !locked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsExpired == null || !credentialsExpired;
    }

    public boolean isActive() {
        return active == null || active;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(Role role : getRoles()) {
            String roleName = "ROLE_"+role.getCode().toUpperCase();
            authorities.add(new SimpleGrantedAuthority(roleName));

            for(Privilege privilege: role.getPrivileges()) {
                roleName = privilege.getCode().toUpperCase();
                authorities.add(new SimpleGrantedAuthority(roleName));
            }
        }
        return authorities;
    }
}