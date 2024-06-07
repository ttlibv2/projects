package vn.conyeu.ts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ListStringToString;
import vn.conyeu.common.domain.LongUId;

import java.util.List;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "softwareId"))
//@formatter:on
public class Software extends LongUId<Software> {

    @Column(length = 50, nullable = false)
    private String code;

    @Column(length = 500, nullable = false)
    private String value;

    @Convert(converter = ListStringToString.class)
    @Column(length = 500, nullable = false)
    private List<String> softNames;

}