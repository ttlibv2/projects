package vn.conyeu.ts.domain;

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
@AttributeOverride(name = "id", column = @Column(name = "chanelId"))
//@formatter:on
public class Chanel extends LongUId<Chanel> {

    @Column(length = 20, nullable = false)
    private String code;

    @Column(length = 20, nullable = false)
    private String extendCode;

    @Column(length = 20, nullable = false)
    private String support;

    @Column(nullable = false)
    private String value;

    @ColumnDefault("0")
    private Boolean hasImage;


}