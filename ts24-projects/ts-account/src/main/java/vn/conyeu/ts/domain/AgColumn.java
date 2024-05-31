package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ObjectMapToString;
import vn.conyeu.common.domain.LongId;
import vn.conyeu.commons.beans.ObjectMap;

//@formatter:off
@Entity @Table(uniqueConstraints = @UniqueConstraint(name = "COL_UID", columnNames = "tableId, fieldName"))
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"table"})
@AttributeOverride(name = "id", column = @Column(name = "columnId"))
//@formatter:on
public class AgColumn extends LongId<AgColumn> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tableId", nullable = false)
    private AgTable table;

    @JsonProperty("field")
    @Column(length = 50, nullable = false)
    private String fieldName;

    @Column(length = 4)
    private Double width;

    @Column(length = 100, nullable = false)
    private String headerName;

    @ColumnDefault("0")
    private Boolean hide;

    @JsonProperty("index")
    private Integer position;

    @ColumnDefault("1")
    private Boolean sort;

    @ColumnDefault("1")
    private Boolean resizable;

    @Column(length = 50)
    private String pinned;

    /**The column type */
    @Column(length = 50)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private AgColumn parent;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap custom;

    @JsonProperty("column_id")
    public Long getId() {
        return super.getId();
    }

    @JsonProperty("index")
    public Integer getPosition() {
        return position != null ? position
                : id != null ? getId().intValue() : null;
    }

    @JsonAnyGetter
    public ObjectMap getCustom() {
        custom = ObjectMap.ifNull(custom);
        return custom;
    }

    @JsonAnySetter
    public void set(String field, Object data) {
        getCustom().set(field, data);
    }
}