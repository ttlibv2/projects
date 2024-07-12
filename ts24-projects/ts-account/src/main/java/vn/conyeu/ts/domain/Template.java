package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUIdDate;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.common.converter.MapString;

//@formatter:off
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Entity @Table(uniqueConstraints = @UniqueConstraint(name = "UID_USER", columnNames = {"entityCode","title","userId"}))
@AttributeOverride(name = "id", column = @Column(name = "templateId"))
@JsonIgnoreProperties({"user"})
//@formatter:on
public class Template extends LongUIdDate<Template> {

	@JsonProperty("entity_code")
	@Column(length = 50, nullable = false)
	private String entityCode;

	@Column(length = 150, nullable = false)
	private String title;

	@Column(length = 20)
	private String icon;

	@Column(length = 300)
	private String summary;
	
	@JsonProperty("bg_color")
	private String bgColor;
	
	@JsonProperty("text_color")
	private String textColor;

	@ColumnDefault("0")
	private Boolean shared;

	@Convert(converter = MapString.class)
	@Column(columnDefinition = "json")
	private ObjectMap data;

	@Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap custom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private TsUser user;

	@ColumnDefault("0")
	private Long position;

	@ColumnDefault("0")
	@JsonProperty("is_default")
	private Boolean isDefault;

	@JsonProperty("template_id")
	public Long getId() {
		return super.getId();
	}

	@JsonProperty("user_id")
	public Long getUserId() {
		return user == null ? null : user.getId();
	}

	/**
	 * Returns the position
	 */
	public Long getPosition() {
		return position == null || position == 0 ? getId() : position;
	}

	public ObjectMap getData() {
		data = ObjectMap.ifNull(data);
		return data;
	}

	/**
     * Returns the custom
     */
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