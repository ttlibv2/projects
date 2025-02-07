package vn.conyeu.book.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ValueToString;
import vn.conyeu.common.domain.LongId;
import vn.conyeu.common.domain.ValueDb;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "seoId"))
//@formatter:on
public class SeoAlias extends LongId<SeoAlias> {
    private String alias;
    private String keywords;
    private String target;
}