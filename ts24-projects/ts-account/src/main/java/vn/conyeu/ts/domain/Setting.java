package vn.conyeu.ts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ObjectMapToString;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "settingId"))
//@formatter:on
public class Setting extends LongUId<Setting> {

    @Column(length = 100, nullable = false)
    private String fieldKey;

    @Column(length = 100, nullable = false)
    private String fieldType;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap data;

}