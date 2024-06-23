package vn.conyeu.ts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.Converts;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.common.domain.ValueDb;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "configId"))
//@formatter:on
public class UserConfig extends LongUId<UserConfig> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private TsUser user;

    @Column(length = 100, nullable = false)
    private String code;

    @Convert(converter = Converts.ValueToString.class)
    @Column(length = 1000, nullable = false)
    private ValueDb value;

}