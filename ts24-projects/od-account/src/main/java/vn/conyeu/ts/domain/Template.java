package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.common.domain.LongUIdDate;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.common.converter.MapString;

//@formatter:off
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Entity @Table()
@AttributeOverride(name = "id", column = @Column(name = "templateId"))
@JsonIgnoreProperties({"user"})
//@formatter:on
public class Template extends LongUId<Template> {

	@Column(length = 150, nullable = false)
	private String title;

	@JsonProperty("thread")
	@Column(length = 30, nullable = false, updatable = false)
	private String thread;

	@Column(length = 300)
	private String summary;

	@ColumnDefault("0")
	private Boolean shared;

	@ColumnDefault("0")
	private Long position;

	@ColumnDefault("0")
	@JsonProperty("is_default")
	private Boolean isDefault;

	@Convert(converter = MapString.class)
	@Column(columnDefinition = "json")
	private ObjectMap data;

	@Convert(converter = MapString.class)
	@Column(columnDefinition = "json")
	private ObjectMap custom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private TsUser user;

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

	@JsonAnyGetter
	public ObjectMap getCustom() {
		custom = ObjectMap.ifNull(custom);
		return custom;
	}

	@JsonAnySetter
	public void set(String field, Object value) {
		getCustom().set(field, value);
	}

	public ObjectMap getData() {
		data = ObjectMap.ifNull(data);
		return data;
	}

}