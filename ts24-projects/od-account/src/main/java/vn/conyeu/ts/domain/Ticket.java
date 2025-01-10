package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.ListLongToString;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.domain.LongUIdDate;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.dtocls.Converters.*;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.ticket.domain.*;

import java.util.List;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"user"})
@AttributeOverride(name = "id", column = @Column(name = "ticketId"))
//@formatter:on
public class Ticket extends LongUIdDate<Ticket> {

    @JsonProperty("ticket_on")
    @Column(length = 150, nullable = false)
    private String ticketOn;

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

    @Column(length = 50)
    private String source;

    @Column(length = 100)
    @JsonProperty("client_version")
    private String clientVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "userId")
    private TsUser user;

    @Column(length = 150)
    private String images;

    @Convert(converter = ClsAssignConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_assign")
    private ClsUser odAssign;

    @Convert(converter = ClsCategorySubConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_category_sub")
    private ClsCategorySub odCateSub;

    @Convert(converter = ClsCategoryConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_category")
    private ClsCategory odCate;

    @Convert(converter = ClsPartnerConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_partner")
    private ClsPartner odPartner;

    @Convert(converter = ClsTicketPriorityConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_priority")
    private ClsTicketPriority odPriority;

    @Convert(converter = ClsRepiledStatusConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_replied")
    private ClsRepliedStatus odReplied;

    @Convert(converter = ClsSubjectTypeConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_subject_type")
    private ClsSubjectType odSubjectType;

    @Convert(converter = ClsTicketTagConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_tags")
    private List<ClsTicketTag> odTags;

    @Convert(converter = ClsHelpdeskTeamConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_team")
    private ClsHelpdeskTeam odTeam;

    @Convert(converter = ClsTeamHeadConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_team_head")
    private ClsTeamHead odTeamHead;

    @Convert(converter = ClsTicketTypeConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_ticket_type")
    private ClsTicketType odTicketType;

    @Convert(converter = ClsTopicConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_topic")
    private ClsTopic odTopic;

    @Convert(converter = ClsProductConvert.class)
    @Column(columnDefinition = "json")
    @JsonProperty("od_product")
    private ClsProduct odProduct;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap options;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap custom;

    @JsonProperty("email_html")
    @Column(columnDefinition = "longtext")
    private String emailHtml;

    @JsonProperty("email_template")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emailTemplate")
    private Template emailTemplate;

    @JsonProperty("email_object")
    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap emailObject;

    @JsonUnwrapped
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ticket")
    private TicketDetail detail;

    @JsonProperty("template_id")
    private Long templateId;

    //-------------------@Transient

    //@JsonProperty("group_helpid") @Transient private Long groupHelpId;
    //@JsonProperty("support_helpid") @Transient  private Long supportHelpId;
    //@JsonProperty("software_id") @Transient private Long softwareId;
    //@JsonProperty("question_id") @Transient private Long questionId;
    @JsonProperty("chanels") @Transient private List<Chanel> chanels;
    @JsonProperty("imageBase64") @Transient private ObjectMap imageBase64;

    public void setStage(ClsStage stage) {
        getDetail().setStage(stage);
    }

    public void setAccessToken(String token) {
        set("access_token", token);
    }

    public void setReportToken(String token) {
        set("report_token", token);
    }

    public ObjectMap getEmailObject() {
        if(emailObject == null) emailObject = new ObjectMap();
        return emailObject;
    }

    @JsonProperty("edit_note")
    public boolean isEditNote() {
        return editNote == null || editNote;
    }

    @JsonProperty("edit_ticket")
    public boolean isEditTicket() {
        return editTicket == null || editTicket;
    }

    @JsonProperty("edit_reply")
    public boolean isEditReply() {
        return editReply == null || editReply;
    }

    @JsonProperty("is_web")
    public boolean isWeb() {
        return "web".equals(source);
    }

    /**
     * Returns the custom
     */
    //@JsonAnyGetter
    public ObjectMap getCustom() {
        custom = ObjectMap.ifNull(custom);
        return custom;
    }

    //@JsonAnySetter
    public Ticket set(String field, Object data) {
        getCustom().set(field, data);
        return this;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return user == null ? null : user.getId();
    }

    /**
     * Returns the odPartner
     */
    public Long getOdPartnerId() {
        return odPartner == null ? null : odPartner.getId();
    }

    @JsonProperty("ticket_id")
    public Long getId() {
        return super.getId();
    }

    /**
     * Returns the detail
     */
    public TicketDetail getDetail() {
        if(detail == null) {
            detail = new TicketDetail();
            detail.setTicket(this);
        }
        return detail;
    }

    @PostPersist
    protected void postPersist() {
        if(detail != null && detail.getTicket() != this) {
            detail.setTicket(this);
        }
    }

    public boolean isSendTicket() {
        return getDetail().getTicketNumber() != null;
    }

    @JsonIgnore
    public String getBodyHtml() {
        return body != null ? body.replace("\n", "<br/>") : null;
    }

    @JsonIgnore
    public String getReplyHtml() {
        return reply != null ? reply.replace("\n", "<br/>") : null;
    }

    @JsonIgnore
    public String getNoteHtml() {
        return note != null ? note.replace("\n", "<br/>") : null;
    }

    public boolean isEmailTicket() {
        return Objects.notBlank(emailHtml);
    }

    @Override
    public void assignFromMap(ObjectMap map) {
        TicketDetail detail = getDetail();

        super.assignFromMap(map);

        if(map.containsKey("images")) {
          //  setImages(map.getString("images"));
        }


        detail.assignFromMap(map);
        setDetail(detail);
    }


    public TicketDetail setNewDetail() {
        TicketDetail detailOld = getDetail();
        TicketDetail detail = new TicketDetail();
        detail.setTicket(this);
        setDetail(detail);
        return  detailOld;
    }

}