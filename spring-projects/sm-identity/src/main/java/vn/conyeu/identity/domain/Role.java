package vn.conyeu.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongId;
import vn.conyeu.common.domain.StringId;

import java.util.HashSet;
import java.util.Set;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "roleId"))
//@formatter:on
public class Role extends LongId<Role> {

    @Column(length = 50, nullable = false)
    private String code;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(length = 300)
    private String summary;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rolePrivilege",
            joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "privilegeId"))
    private Set<Privilege> privileges;

    public Set<Privilege> getPrivileges() {
        if(privileges == null) privileges = new HashSet<>();
        return privileges;
    }
}