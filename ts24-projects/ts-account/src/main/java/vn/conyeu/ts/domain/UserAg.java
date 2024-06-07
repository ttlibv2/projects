package vn.conyeu.ts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ObjectMapToString;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "agId"))
//@formatter:on
public class UserAg extends LongUId<UserAg> {

    @Column(length = 50, nullable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String title;

    @ColumnDefault("1")
    private Integer position;

    @ColumnDefault("1")
    private Boolean active;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap config;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tableId", nullable = false)
    private AgTable table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private TsUser user;


}