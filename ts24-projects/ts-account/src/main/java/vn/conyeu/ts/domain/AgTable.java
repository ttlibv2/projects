package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;

import java.util.ArrayList;
import java.util.List;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"parent", "user"})
@AttributeOverride(name = "id", column = @Column(name = "tableId"))
//@formatter:on
public class AgTable extends LongUId<AgTable> {

    @Column(length = 50, nullable = false, unique = true)
    private String code;

    @Column(length = 100)
    private String title;

    @Column(length = 300)
    private String summary;

    @Column(length = 100)
    @JsonProperty("svg_icon")
    private String svgIcon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private TsUser user;

    @ColumnDefault("0")
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private AgTable parent;

    @JsonIgnore
    @JsonAnySetter
    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap config;

    @Transient
    private List<AgTable> children;

    /**
     * Returns the children
     */
    public List<AgTable> getChildren() {
        if(children == null) children = new ArrayList<>();
        return children;
    }

    @JsonAnyGetter
    public ObjectMap getConfig() {
        if(config == null) config = new ObjectMap();
        return config;
    }

    public Object get(String field) {
        return getConfig().get(field);
    }

    @JsonProperty("table_id")
    public Long getId() {
        return super.getId();
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return user == null ? null : user.getId();
    }

    @JsonProperty("parent_id")
    public Long getParentId() {
        return parent == null ? null : parent.getId();
    }

    public AgTable createChild() {
        AgTable table = new AgTable();
        table.setParent(this);
        getChildren().add(table);
        return table;
    }
}