package vn.conyeu.address.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "wardId"))
//@formatter:on
public class Ward extends LongUId<Ward> {

    @Column(length = 10, nullable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String name;

    @ColumnDefault("0")
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "districtId")
    private District district;
}