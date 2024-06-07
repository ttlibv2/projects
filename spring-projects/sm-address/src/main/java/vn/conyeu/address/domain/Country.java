package vn.conyeu.address.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@AttributeOverride(name = "id", column = @Column(name = "countryId"))
//@formatter:on
public class Country extends LongUId<Country> {
    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 2, nullable = false)
    private String isoCode2;

    @Column(length = 3, nullable = false)
    private String isoCode3;

    @Column(length = 3)
    private String currency;

    @Column(length = 3)
    private String phoneCode;

    @ColumnDefault("0")
    private Integer position;
}