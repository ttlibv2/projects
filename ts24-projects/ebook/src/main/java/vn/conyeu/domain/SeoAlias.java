package vn.conyeu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;

//@formatter:off
@Entity
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Table(name = "eb_seoalias", indexes = @Index(name = "UK_MODEL", columnList = "model,modelId", unique = true))
@AttributeOverride(name = "id", column = @Column(name = "seoId"))
//@formatter:on
public class SeoAlias extends LongUId<SeoAlias> {
    private String model;
    private Long modelId;
    private String alias;
    private String title;
    private String description;
    private String keywords;

    public SeoAlias(String model) {
        this.model = model;
    }


}