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
@AttributeOverride(name = "id", column = @Column(name = "provinceId"))
//@formatter:on
public class Province extends LongUId<Province> {
    @Column(length = 50, nullable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String name;

    private String type;

    @Column(length = 3)
    private String phoneCode;

    @ColumnDefault("0")
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    private Country country;
}