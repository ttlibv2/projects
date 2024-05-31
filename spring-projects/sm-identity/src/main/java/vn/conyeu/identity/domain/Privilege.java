package vn.conyeu.identity.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongId;
import vn.conyeu.common.domain.StringId;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "privilegeId"))
//@formatter:on
public class Privilege extends LongId<Privilege> {

    @Column(length = 50, nullable = false)
    private String code;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(length = 300)
    private String summary;

    @Column(nullable = false, length = 50)
    private String entity;
}