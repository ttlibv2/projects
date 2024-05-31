package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ListLongToString;
import vn.conyeu.common.converter.ObjectMapToString;
import vn.conyeu.common.domain.LongId;
import vn.conyeu.common.domain.LongIdDate;
import vn.conyeu.commons.beans.ObjectMap;

import java.util.List;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"user"})
//@formatter:on
public class Ticket extends LongIdDate<Ticket> {

    @JsonProperty("full_name")
    @Column(length = 150, nullable = false)
    private String fullName;

    @JsonProperty("tax_code")
    @Column(length = 15)
    private String taxCode;

    @Column(length = 500)
    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("phone")
    @Column(length = 150)
    private String phone;

    @JsonProperty("teamviewer")
    @Column(length = 150)
    private String teamviewer;

    @Column(length = 500)
    @JsonProperty("customer_name")
    private String customerName;

    // Noi dung khach hang yeu cau
    @Column(columnDefinition = "longtext")
    @JsonProperty("content_required")
    private String contentRequired;

    // Noi dung da ho tro
    @Column(columnDefinition = "longtext")
    @JsonProperty("content_help")
    private String contentHelp;

    // Thời gian tiếp nhận
    @JsonProperty("reception_time")
    private String receptionTime;

    // Thời gian hoàn thành
    @JsonProperty("complete_time")
    private String completeTime;

    @JsonProperty("content_copy")
    @Column(columnDefinition = "longtext")
    private String contentCopy;

    private String email;

    @Column(name = "`subject`")
    private String subject;

    @Column(columnDefinition = "longtext")
    private String body;

    @Column(columnDefinition = "longtext")
    private String note;

    @Column(columnDefinition = "longtext")
    private String reply;

    @JsonProperty("content_email")
    @Column(columnDefinition = "longtext")
    private String contentEmail;

    @JsonProperty("group_help")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helpId")
    private GroupHelp groupHelp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionId")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "softwareId")
    private Software software;

    @JsonProperty("chanel_ids")
    @Convert(converter = ListLongToString.class)
    private List<Long> chanelIds;

    @JsonProperty("support_help")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supportHelpId")
    private Chanel supportHelp;

    @JsonProperty("soft_name")
    private String softName;

    @ColumnDefault("0")
    @JsonProperty("edit_ticket")
    private Boolean editTicket;

    @ColumnDefault("0")
    @JsonProperty("edit_note")
    private Boolean editNote;

    @ColumnDefault("0")
    @JsonProperty("edit_reply")
    private Boolean editReply;

    @ColumnDefault("0")
    @JsonProperty("is_web")
    @Column(name = "isWeb")
    private Boolean web;

    @ColumnDefault("0")
    @JsonProperty("is_delete")
    @Column(name = "isDelete")
    private Boolean delete;

    @ColumnDefault("0")
    @JsonProperty("is_report")
    @Column(name = "isReport")
    private Boolean report;

    @Column(length = 50)
    private String source;

    @Column(length = 100)
	 @JsonProperty("client_version")
    private String clientVersion;

    @Column(length = 20)
    private Long odPartnerId;

    @Enumerated(EnumType.STRING)
	@JsonProperty("ticket_status")
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "userId")
    private TsUser user;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_assign")
    private ObjectMap odAssign;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_category_sub")
    private ObjectMap odCateSub;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_category")
    private ObjectMap odCate;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_image")
    private ObjectMap odImage;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_partner")
    private ObjectMap odPartner;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_priority")
    private ObjectMap odPriority;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_repiled")
    private ObjectMap odRepiled;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_subject_type")
    private ObjectMap odSubjectType;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_tags")
    private ObjectMap odTags;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_team")
    private ObjectMap odTeam;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_team_head")
    private ObjectMap odTeamHead;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_ticket_type")
    private ObjectMap odTicketType;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
	@JsonProperty("od_topic")
    private ObjectMap odTopic;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap options;

    @Convert(converter = ObjectMapToString.class)
    @Column(columnDefinition = "json")
    private ObjectMap custom;

    //@JsonUnwrapped
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ticket")
    private TicketDetail detail;

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
	
	@JsonProperty("user_id")
	public Long getUserId() {
		 return user == null ? null : user.getId();
	}

}