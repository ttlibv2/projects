package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.common.converter.ObjectMapToString;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"ticket"})
//@formatter:on
public class TicketTemplate extends LongUId<TicketTemplate> {
	private String code;
	private String title;
	private String icon;
	private String summary;
	
	@JsonProperty("bg_color")
	private String bgColor;
	
	@JsonProperty("text_color")
	private String textColor;
	
	@MapsId @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketId")
    private Ticket ticket;
	
	@Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap custom;
	
	
	@JsonProperty("template_id")
	public Long getTemplateId() {
		 return ticket == null ? null : ticket.getId();
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