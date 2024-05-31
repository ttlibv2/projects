package vn.conyeu.account.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongId;

import java.util.Set;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "roleId"))
//@formatter:on
public class Role extends LongId<Role> {
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String title;

    private String summary;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rolePrivilege",
            joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "privilegeId"))
    private Set<Privilege> privileges;
}