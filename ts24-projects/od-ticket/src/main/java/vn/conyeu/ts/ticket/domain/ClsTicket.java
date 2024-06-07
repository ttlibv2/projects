package vn.conyeu.ts.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsHelper;
import vn.conyeu.ts.odcore.domain.ClsModel;
import vn.conyeu.ts.odcore.helper.OdHelper;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
public class ClsTicket extends ClsModel<ClsTicket> {

    static final Set<String> FIELD_IGNORE = Set.of(
            "stage", "category", "categorySub",
            "helpdeskTeam", "subjectType", "ticketType", "partner",
            "clsTeamHead"
    );

    private Long id;
    private String name;
    private String display_name;
    private String subject;
    private Object partner_id;
    private Object category_id; // id | [id,name]
    private Object priority; // độ ưu tiên
    private Object stage_id; // tình trạng id | [id,name]
    private Object user_id;
    private String create_date;
    private Object company_id;
    private Object team_id;
    private Object topic_id;

    private Integer sale_order_count;
    private Integer purchase_order_count;
    private Integer invoice_count;
    private Integer lead_count;
    private Integer opportunity_count;
    private Integer task_count;

    private Object active;
    private Object category_bool;
    private Object sub_category_bool;
    private Object rating_bool;
    private Object sh_status_boolean;
    private Object done_stage_boolean;
    private Object cancel_stage_boolean;
    private Object closed_stage_boolean;
    private Object ticket_from_website;
    private Object ticket_from_portal;
    private Object reopen_stage_boolean;
    private Object cancel_button_boolean;
    private Object done_button_boolean;
    private Object open_boolean;
    private Integer sh_days_to_reach;
    private Integer sh_days_to_late;


    private String sh_ticket_report_url;//	"http://web.ts24.com.vn/download/ht/1855?access_token=ce05e45e-2665-407d-a62c-77a911d206e5"
    private String portal_ticket_url_wp;//	"http://web.ts24.com.vn/my/tickets/1855?access_token=f5eb73e9-4091-4025-b41a-4369c4e8242a"
    private String email_subject;//	"[HT-PI]-VTCUC-0302861679-20220914"
    private Object sh_sla_status_ids;//	[]
    private Object sh_display_multi_user;//	false
    private Object sh_display_product;//	true
    //   private Object  company_id;//	[ 1, "CÔNG TY CỔ PHẦN TS24" ]
    String state;//"staff_replied"
    private Object ticket_type;//	false
    private Object ticket_allocated;//	true
    // private Object   team_id	;//[ 6, "Hỗ trợ kỹ thuật" ]
    private Object team_head;//[ 66, "Nguyễn Bảo Nguyên Minh" ]
    //  private Object user_id	;//[ 57, "Vũ Thị Cúc" ]
    private Object sh_user_ids;//[]
    private Object subject_id;//[ 6, "iBHXH | Bảo hiểm xã hội điện tử" ]
    //  private Object category_id;//	[ 13, "Hỗ trợ" ]
    private Object sub_category_id;//[ 6, "Hỗ trợ kỹ thuật" ]
    private Object tag_ids;//[]
    //  Boolean  priority	;//false
    //   private String  create_date	;//"2022-09-14 09:07:39"
    private String write_date;//"2022-09-14 09:18:04"
    private String sh_due_date;//	"2022-09-14 09:07:08"
    //  private Object  partner_id	;//[ 116875, "CÔNG TY TNHH XÂY DỰNG - THƯƠNG MẠI - DỊCH VỤ THUẦN PHONG, Nguyễn Thị Giáng Hương" ]
    private String person_name;//"Nguyễn Thị Giáng Hương"
    private String email;//"missthe88@gmail.com"
    private String mobile_no;//"0932192636"
    private String replied_date;//"2022-09-14 09:08:10"
    private Object task_ids;//[]
    private Object product_ids;//[]
    private Object sh_lead_ids;//	[]
    private Object sh_invoice_ids;//[]
    private Object sh_purchase_order_ids;//[]
    private Object sh_sale_order_ids;//[]
    private Object sh_status;//	false
    private Object sh_sla_deadline;//	false
    private Object sh_sla_policy_ids;//	[]
    private Object sh_ticket_alarm_ids;//	[]
    private String description;//	"<p><br></p>"
    private Object attachment_ids;//	[]
    private Object priority_new;//	false
    private Object customer_comment;//false
    private Object close_date;//	false
    private Object close_by;//false
    private Object comment;//	false
    private Object cancel_date;//false
    private Object cancel_by;//	false
    private Object cancel_reason;//	false
    private String form_url;//	"http://web.ts24.com.vn/web#id=1855&action=848&model=helpdesk.ticket&view_type=form"
    private Object message_follower_ids;//	[ 9314, 9316 ]
    private Object message_ids;//	[ 27889, 27761, 27755, 27754, 27739, 27738, 27729, 27728, 27727 ]
    private Object activity_ids;//	[]
    // private String display_name;//	"TICKET/1855"
    private String access_token;
    private String report_token;

    private ClsStage stage;
    private ClsCategory category;
    private ClsCategorySub categorySub;
    private ClsHelpdeskTeam helpdeskTeam;
    private ClsTeamHead clsTeamHead;
    private ClsSubjectType subjectType;
    private ClsTicketType ticketType;
    private ClsPartner partner;

    @JsonIgnore
    public ClsStage getOrNewStage() {
        return stage == null ? new ClsStage() : stage;
    }

    public ClsTicket stage_id(Object stage_id) {
        this.stage_id = stage_id;
        this.stage = ClsStage.from(stage_id);
        return this;
    }

    public ClsTicket category_id(Object category_id) {
        this.category_id = category_id;
        this.category = ClsCategory.from(category_id);
        return this;
    }

    public ClsTicket sub_category_id(Object sub_category_id) {
        this.sub_category_id = sub_category_id;
        this.categorySub = ClsCategorySub.from(sub_category_id);
        return this;
    }

    public ClsTicket team_id(Object team_id) {
        this.team_id = team_id;
        this.helpdeskTeam = ClsHelpdeskTeam.from(team_id);
        this.clsTeamHead = team_id == null ? null : this.helpdeskTeam.getClsTeamHead();
        return this;
    }

    public ClsTicket subject_id(Object subject_id) {
        this.subject_id = subject_id;
        this.subjectType = ClsSubjectType.from(subject_id);
        return this;
    }

    public ClsTicket ticket_type(Object ticket_type) {
        this.ticket_type = ticket_type;
        this.ticketType = ClsTicketType.from(ticket_type);
        return this;
    }

    public ClsTicket partner_id(Object partner_id) {
        this.partner_id = partner_id;
        this.partner = ClsPartner.from(partner_id);
        return this;
    }

    public static ClsTicket from(ObjectMap map) {
        ClsTicket clsTicket = map.asObject(ClsTicket.class);
        clsTicket.set("ticket_web", map);
        clsTicket.setTeam_id(clsTicket.team_id);
        clsTicket.stage = ClsStage.from(clsTicket.stage_id);
        clsTicket.category = ClsCategory.from(clsTicket.category_id);
        clsTicket.categorySub = ClsCategorySub.from(clsTicket.sub_category_id);
        clsTicket.subjectType = ClsSubjectType.from(clsTicket.subject_id);
        clsTicket.ticketType = ClsTicketType.from(clsTicket.ticket_type);
        clsTicket.partner = ClsPartner.from(clsTicket.partner_id);

        if (clsTicket.helpdeskTeam != null) {
            clsTicket.helpdeskTeam.setTeam_head(clsTicket.team_head);
        }

        return clsTicket;
    }

//    public static ClsTicket fromV15(Ticket ticket) {
//        ClsTicket cls = new ClsTicket();
//
//        cls.setStage_id(4);
//        cls.setActive(true);
//        cls.setTicket_from_website(false);
//        cls.setTicket_from_portal(false);
//        cls.setEmail_subject(ticket.getSubject());
//        cls.setSh_sla_status_ids(new Object[0]);
//        cls.setTicket_allocated(false);
//
//        ClsRepiledStatus repiledStatus = ticket.getOdRepiledStatus();
//        if(repiledStatus != null) cls.setState(repiledStatus.getCode());
//
//        ClsTicketType ticketType = ticket.getOdTicketType();
//        if(ticketType != null) cls.setTicket_type(ticketType.getId());
//
//        ClsUser clsUser = ticket.getOdAssign();
//        if(clsUser != null) cls.setUser_id(clsUser.getId());
//
//        ClsSubjectType subType = ticket.getOdSubjectType();
//        if(subType != null) cls.setSubject_id(subType.getId());
//
//        ClsCategory clsCategory = ticket.getOdCategory();
//        if(clsCategory != null) cls.setCategory_id(clsCategory.getId());
//
//        ClsCategorySub clsCategorySub = ticket.getOdCateSub();
//        if(clsCategorySub != null) cls.setSub_category_id(clsCategorySub.getId());
//
//        ClsTicketPriority clsTicketPriority = ticket.getOdPriority();
//        if(clsTicketPriority != null) cls.setPriority(clsTicketPriority.getId());
//
//        List<ClsTicketTag> clsTicketTags = ticket.getOdTags();
//        if(clsTicketTags != null && !clsTicketTags.isEmpty()) {
//            Object[] tagsId = clsTicketTags.stream().map(ClsTicketTag::getId).toArray();
//            cls.setTag_ids(createList(6, false, tagsId));
//        }
//
//        ClsHelpdeskTeam clsTeam = ticket.getOdTeam();
//        if(clsTeam != null)cls.setTeam_id(clsTeam.getId());
//
//        ClsTeamHead clsTeamHead = ticket.getOdTeamHead();
//        if(clsTeamHead != null)cls.setTeam_head(clsTeamHead.getId());
//
//        cls.setPartner_id(ticket.getOdKhId());
//        cls.setPerson_name(ticket.getCustomerName());
//        cls.setEmail(ticket.getEmail());
//        cls.setMobile_no(ticket.getPhone());
//        cls.setSh_status(false);
//
//        //không tạo body 10/03/2023 -> thêm body vào note
//        //cls.setDescription(detail.getBodyHtml());
//
//        cls.setPriority_new(false);
//        cls.setCustomer_comment(false);
//        cls.setClose_date(false);
//        cls.setClose_by(false);
//        cls.setComment(false);
//        cls.setCancel_date(false);
//        cls.setCancel_reason(false);
//        cls.setMessage_follower_ids(new Object[0]);
//        cls.setMessage_ids(new Object[0]);
//        cls.setActivity_ids(new Object[0]);
//
//        cls.setSh_user_ids(createList(6, false, new Object[0]));
//        cls.setTask_ids(createList(6, false, new Object[0]));
//        cls.setProduct_ids(createList(6, false, new Object[0]));
//        cls.setSh_lead_ids(createList(6, false, new Object[0]));
//        cls.setSh_invoice_ids(createList(6, false, new Object[0]));
//        cls.setSh_purchase_order_ids(createList(6, false, new Object[0]));
//        cls.setSh_sale_order_ids(createList(6, false, new Object[0]));
//        cls.setSh_sla_policy_ids(createList(6, false, new Object[0]));
//        cls.setSh_ticket_alarm_ids(createList(6, false, new Object[0]));
//        cls.setAttachment_ids(createList(6, false, new Object[0]));
//
//        return cls;
//    }

    private static Object[] createList(Object... args) {
        return new Object[]{args};
    }


//    public static ClsTicket fromV12(ClsUser odooUser, Ticket ticket) {
//
//        ClsTicket cls = new ClsTicket();
//        cls.setStage_id(1);
//        cls.setPartner_id(ticket.getOdKhId());
//        cls.setSubject(ticket.getSubject());
//        cls.set("kanban_state", "normal")
//                .set("company_id", odooUser.getCompany_id())
//                .set("priority", 1)
//                .set("active", true)
//                .set("resolve", false)
//                .set("cancel", false)
//                .set("date_deadline", false)
//                .set("message_attachment_count", 0)
//                .set("description", ticket.getBodyHtml())
//                .set("contact_name", ticket.getCustomerName())
//                .set("email", ticket.getEmail())
//                .set("user_id", ticket.getOdAssign().getId())
//                .set("team_id", ticket.getOdTeam().getId())
//                .set("category_id", ticket.getOdCategory().getId())
//                .set("topic_id", ticket.getOdTopic().getId());
//
//        return cls;
//    }

    @Override
    public Set<String> fieldCloneIgnore() {
        return super.fieldCloneIgnore();
    }

    @Override
    public ObjectMap cloneMap() {
        return super.cloneMap();//.keyVal("priority", priority);
    }

    public Object[] toArgRequest() {
        return new Object[]{cloneMap()};
    }

    @JsonIgnore
    public Long getStageId() {
        return getObjectID(stage_id);
    }

    /**
     * Returns the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the display_name
     */
    public String getDisplay_name() {
        return display_name;
    }

    /**
     * Returns the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Returns the partner_id
     */
    public Object getPartner_id() {
        return partner_id;
    }

    /**
     * Returns the category_id
     */
    public Object getCategory_id() {
        return category_id;
    }

    /**
     * Returns the priority
     */
    public Object getPriority() {
        return priority;
    }

    /**
     * Returns the stage_id
     */
    public Object getStage_id() {
        return stage_id;
    }

    /**
     * Returns the user_id
     */
    public Object getUser_id() {
        return user_id;
    }

    /**
     * Returns the create_date
     */
    public String getCreate_date() {
        return create_date;
    }

    /**
     * Returns the company_id
     */
    public Object getCompany_id() {
        return company_id;
    }

    /**
     * Returns the team_id
     */
    public Object getTeam_id() {
        return team_id;
    }

    /**
     * Returns the topic_id
     */
    public Object getTopic_id() {
        return topic_id;
    }

    /**
     * Returns the sale_order_count
     */
    public Integer getSale_order_count() {
        return sale_order_count;
    }

    /**
     * Returns the purchase_order_count
     */
    public Integer getPurchase_order_count() {
        return purchase_order_count;
    }

    /**
     * Returns the invoice_count
     */
    public Integer getInvoice_count() {
        return invoice_count;
    }

    /**
     * Returns the lead_count
     */
    public Integer getLead_count() {
        return lead_count;
    }

    /**
     * Returns the opportunity_count
     */
    public Integer getOpportunity_count() {
        return opportunity_count;
    }

    /**
     * Returns the task_count
     */
    public Integer getTask_count() {
        return task_count;
    }

    /**
     * Returns the active
     */
    public Object getActive() {
        return active;
    }

    /**
     * Returns the category_bool
     */
    public Object getCategory_bool() {
        return category_bool;
    }

    /**
     * Returns the sub_category_bool
     */
    public Object getSub_category_bool() {
        return sub_category_bool;
    }

    /**
     * Returns the rating_bool
     */
    public Object getRating_bool() {
        return rating_bool;
    }

    /**
     * Returns the sh_status_boolean
     */
    public Object getSh_status_boolean() {
        return sh_status_boolean;
    }

    /**
     * Returns the done_stage_boolean
     */
    public Object getDone_stage_boolean() {
        return done_stage_boolean;
    }

    /**
     * Returns the cancel_stage_boolean
     */
    public Object getCancel_stage_boolean() {
        return cancel_stage_boolean;
    }

    /**
     * Returns the closed_stage_boolean
     */
    public Object getClosed_stage_boolean() {
        return closed_stage_boolean;
    }

    /**
     * Returns the ticket_from_website
     */
    public Object getTicket_from_website() {
        return ticket_from_website;
    }

    /**
     * Returns the ticket_from_portal
     */
    public Object getTicket_from_portal() {
        return ticket_from_portal;
    }

    /**
     * Returns the reopen_stage_boolean
     */
    public Object getReopen_stage_boolean() {
        return reopen_stage_boolean;
    }

    /**
     * Returns the cancel_button_boolean
     */
    public Object getCancel_button_boolean() {
        return cancel_button_boolean;
    }

    /**
     * Returns the done_button_boolean
     */
    public Object getDone_button_boolean() {
        return done_button_boolean;
    }

    /**
     * Returns the open_boolean
     */
    public Object getOpen_boolean() {
        return open_boolean;
    }

    /**
     * Returns the sh_days_to_reach
     */
    public Integer getSh_days_to_reach() {
        return sh_days_to_reach;
    }

    /**
     * Returns the sh_days_to_late
     */
    public Integer getSh_days_to_late() {
        return sh_days_to_late;
    }

    /**
     * Returns the sh_ticket_report_url
     */
    public String getSh_ticket_report_url() {
        return sh_ticket_report_url;
    }

    /**
     * Returns the portal_ticket_url_wp
     */
    public String getPortal_ticket_url_wp() {
        return portal_ticket_url_wp;
    }

    /**
     * Returns the email_subject
     */
    public String getEmail_subject() {
        return email_subject;
    }

    /**
     * Returns the sh_sla_status_ids
     */
    public Object getSh_sla_status_ids() {
        return sh_sla_status_ids;
    }

    /**
     * Returns the sh_display_multi_user
     */
    public Object getSh_display_multi_user() {
        return sh_display_multi_user;
    }

    /**
     * Returns the sh_display_product
     */
    public Object getSh_display_product() {
        return sh_display_product;
    }

    /**
     * Returns the state
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the ticket_type
     */
    public Object getTicket_type() {
        return ticket_type;
    }

    /**
     * Returns the ticket_allocated
     */
    public Object getTicket_allocated() {
        return ticket_allocated;
    }

    /**
     * Returns the team_head
     */
    public Object getTeam_head() {
        return team_head;
    }

    /**
     * Returns the sh_user_ids
     */
    public Object getSh_user_ids() {
        return sh_user_ids;
    }

    /**
     * Returns the subject_id
     */
    public Object getSubject_id() {
        return subject_id;
    }

    /**
     * Returns the sub_category_id
     */
    public Object getSub_category_id() {
        return sub_category_id;
    }

    /**
     * Returns the tag_ids
     */
    public Object getTag_ids() {
        return tag_ids;
    }

    /**
     * Returns the write_date
     */
    public String getWrite_date() {
        return write_date;
    }

    /**
     * Returns the sh_due_date
     */
    public String getSh_due_date() {
        return sh_due_date;
    }

    /**
     * Returns the person_name
     */
    public String getPerson_name() {
        return person_name;
    }

    /**
     * Returns the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the mobile_no
     */
    public String getMobile_no() {
        return mobile_no;
    }

    /**
     * Returns the replied_date
     */
    public String getReplied_date() {
        return replied_date;
    }

    /**
     * Returns the task_ids
     */
    public Object getTask_ids() {
        return task_ids;
    }

    /**
     * Returns the product_ids
     */
    public Object getProduct_ids() {
        return product_ids;
    }

    /**
     * Returns the sh_lead_ids
     */
    public Object getSh_lead_ids() {
        return sh_lead_ids;
    }

    /**
     * Returns the sh_invoice_ids
     */
    public Object getSh_invoice_ids() {
        return sh_invoice_ids;
    }

    /**
     * Returns the sh_purchase_order_ids
     */
    public Object getSh_purchase_order_ids() {
        return sh_purchase_order_ids;
    }

    /**
     * Returns the sh_sale_order_ids
     */
    public Object getSh_sale_order_ids() {
        return sh_sale_order_ids;
    }

    /**
     * Returns the sh_status
     */
    public Object getSh_status() {
        return sh_status;
    }

    /**
     * Returns the sh_sla_deadline
     */
    public Object getSh_sla_deadline() {
        return sh_sla_deadline;
    }

    /**
     * Returns the sh_sla_policy_ids
     */
    public Object getSh_sla_policy_ids() {
        return sh_sla_policy_ids;
    }

    /**
     * Returns the sh_ticket_alarm_ids
     */
    public Object getSh_ticket_alarm_ids() {
        return sh_ticket_alarm_ids;
    }

    /**
     * Returns the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the attachment_ids
     */
    public Object getAttachment_ids() {
        return attachment_ids;
    }

    /**
     * Returns the priority_new
     */
    public Object getPriority_new() {
        return priority_new;
    }

    /**
     * Returns the customer_comment
     */
    public Object getCustomer_comment() {
        return customer_comment;
    }

    /**
     * Returns the close_date
     */
    public LocalDateTime getClose_date() {
        return OdHelper.toDateTime(close_date);
    }

    /**
     * Returns the close_by
     */
    public Object getClose_by() {
        return close_by;
    }

    /**
     * Returns the close_by
     */
    public Long getClose_byId() {
        return getObjectID(close_by);
    }

    /**
     * Returns the comment
     */
    public Object getComment() {
        return comment;
    }

    /**
     * Returns the cancel_date
     */
    public LocalDateTime getCancel_date() {
        return OdHelper.toDateTime(cancel_date);
    }

    /**
     * Returns the cancel_by
     */
    public Object getCancel_by() {
        return cancel_by;
    }

    /**
     * Returns the cancel_reason
     */
    public String getCancel_reason() {
        return OdHelper.toString(cancel_reason);
    }

    /**
     * Returns the form_url
     */
    public String getForm_url() {
        return form_url;
    }

    /**
     * Returns the message_follower_ids
     */
    public Object getMessage_follower_ids() {
        return message_follower_ids;
    }

    /**
     * Returns the message_ids
     */
    public Object getMessage_ids() {
        return message_ids;
    }

    /**
     * Returns the activity_ids
     */
    public Object getActivity_ids() {
        return activity_ids;
    }

    /**
     * Returns the access_token
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * Returns the report_token
     */
    public String getReport_token() {
        return report_token;
    }

    /**
     * Returns the stage
     */
    public ClsStage getStage() {
        return stage;
    }

    /**
     * Returns the category
     */
    public ClsCategory getCategory() {
        return category;
    }

    /**
     * Returns the categorySub
     */
    public ClsCategorySub getCategorySub() {
        return categorySub;
    }

    /**
     * Returns the helpdeskTeam
     */
    public ClsHelpdeskTeam getHelpdeskTeam() {
        return helpdeskTeam;
    }

    /**
     * Returns the clsTeamHead
     */
    public ClsTeamHead getClsTeamHead() {
        return clsTeamHead;
    }

    /**
     * Returns the subjectType
     */
    public ClsSubjectType getSubjectType() {
        return subjectType;
    }

    /**
     * Returns the ticketType
     */
    public ClsTicketType getTicketType() {
        return ticketType;
    }

    /**
     * Returns the partner
     */
    public ClsPartner getPartner() {
        return partner;
    }

    /**
     * Set the id
     *
     * @param id the value
     */
    public ClsTicket setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Set the name
     *
     * @param name the value
     */
    public ClsTicket setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the display_name
     *
     * @param display_name the value
     */
    public ClsTicket setDisplay_name(String display_name) {
        this.display_name = display_name;
        return this;
    }

    /**
     * Set the subject
     *
     * @param subject the value
     */
    public ClsTicket setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Set the partner_id
     *
     * @param partner_id the value
     */
    public ClsTicket setPartner_id(Object partner_id) {
        this.partner_id = partner_id;
        return this;
    }

    /**
     * Set the category_id
     *
     * @param category_id the value
     */
    public ClsTicket setCategory_id(Object category_id) {
        this.category_id = category_id;
        return this;
    }

    /**
     * Set the priority
     *
     * @param priority the value
     */
    public ClsTicket setPriority(Object priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Set the stage_id
     *
     * @param stage_id the value
     */
    public ClsTicket setStage_id(Object stage_id) {
        this.stage_id = stage_id;
        return this;
    }

    /**
     * Set the user_id
     *
     * @param user_id the value
     */
    public ClsTicket setUser_id(Object user_id) {
        this.user_id = user_id;
        return this;
    }

    /**
     * Set the create_date
     *
     * @param create_date the value
     */
    public ClsTicket setCreate_date(String create_date) {
        this.create_date = create_date;
        return this;
    }

    /**
     * Set the company_id
     *
     * @param company_id the value
     */
    public ClsTicket setCompany_id(Object company_id) {
        this.company_id = company_id;
        return this;
    }

    /**
     * Set the team_id
     *
     * @param team_id the value
     */
    public ClsTicket setTeam_id(Object team_id) {
        this.team_id = team_id;
        return this;
    }

    /**
     * Set the topic_id
     *
     * @param topic_id the value
     */
    public ClsTicket setTopic_id(Object topic_id) {
        this.topic_id = topic_id;
        return this;
    }

    /**
     * Set the sale_order_count
     *
     * @param sale_order_count the value
     */
    public ClsTicket setSale_order_count(Integer sale_order_count) {
        this.sale_order_count = sale_order_count;
        return this;
    }

    /**
     * Set the purchase_order_count
     *
     * @param purchase_order_count the value
     */
    public ClsTicket setPurchase_order_count(Integer purchase_order_count) {
        this.purchase_order_count = purchase_order_count;
        return this;
    }

    /**
     * Set the invoice_count
     *
     * @param invoice_count the value
     */
    public ClsTicket setInvoice_count(Integer invoice_count) {
        this.invoice_count = invoice_count;
        return this;
    }

    /**
     * Set the lead_count
     *
     * @param lead_count the value
     */
    public ClsTicket setLead_count(Integer lead_count) {
        this.lead_count = lead_count;
        return this;
    }

    /**
     * Set the opportunity_count
     *
     * @param opportunity_count the value
     */
    public ClsTicket setOpportunity_count(Integer opportunity_count) {
        this.opportunity_count = opportunity_count;
        return this;
    }

    /**
     * Set the task_count
     *
     * @param task_count the value
     */
    public ClsTicket setTask_count(Integer task_count) {
        this.task_count = task_count;
        return this;
    }

    /**
     * Set the active
     *
     * @param active the value
     */
    public ClsTicket setActive(Object active) {
        this.active = active;
        return this;
    }

    /**
     * Set the category_bool
     *
     * @param category_bool the value
     */
    public ClsTicket setCategory_bool(Object category_bool) {
        this.category_bool = category_bool;
        return this;
    }

    /**
     * Set the sub_category_bool
     *
     * @param sub_category_bool the value
     */
    public ClsTicket setSub_category_bool(Object sub_category_bool) {
        this.sub_category_bool = sub_category_bool;
        return this;
    }

    /**
     * Set the rating_bool
     *
     * @param rating_bool the value
     */
    public ClsTicket setRating_bool(Object rating_bool) {
        this.rating_bool = rating_bool;
        return this;
    }

    /**
     * Set the sh_status_boolean
     *
     * @param sh_status_boolean the value
     */
    public ClsTicket setSh_status_boolean(Object sh_status_boolean) {
        this.sh_status_boolean = sh_status_boolean;
        return this;
    }

    /**
     * Set the done_stage_boolean
     *
     * @param done_stage_boolean the value
     */
    public ClsTicket setDone_stage_boolean(Object done_stage_boolean) {
        this.done_stage_boolean = done_stage_boolean;
        return this;
    }

    /**
     * Set the cancel_stage_boolean
     *
     * @param cancel_stage_boolean the value
     */
    public ClsTicket setCancel_stage_boolean(Object cancel_stage_boolean) {
        this.cancel_stage_boolean = cancel_stage_boolean;
        return this;
    }

    /**
     * Set the closed_stage_boolean
     *
     * @param closed_stage_boolean the value
     */
    public ClsTicket setClosed_stage_boolean(Object closed_stage_boolean) {
        this.closed_stage_boolean = closed_stage_boolean;
        return this;
    }

    /**
     * Set the ticket_from_website
     *
     * @param ticket_from_website the value
     */
    public ClsTicket setTicket_from_website(Object ticket_from_website) {
        this.ticket_from_website = ticket_from_website;
        return this;
    }

    /**
     * Set the ticket_from_portal
     *
     * @param ticket_from_portal the value
     */
    public ClsTicket setTicket_from_portal(Object ticket_from_portal) {
        this.ticket_from_portal = ticket_from_portal;
        return this;
    }

    /**
     * Set the reopen_stage_boolean
     *
     * @param reopen_stage_boolean the value
     */
    public ClsTicket setReopen_stage_boolean(Object reopen_stage_boolean) {
        this.reopen_stage_boolean = reopen_stage_boolean;
        return this;
    }

    /**
     * Set the cancel_button_boolean
     *
     * @param cancel_button_boolean the value
     */
    public ClsTicket setCancel_button_boolean(Object cancel_button_boolean) {
        this.cancel_button_boolean = cancel_button_boolean;
        return this;
    }

    /**
     * Set the done_button_boolean
     *
     * @param done_button_boolean the value
     */
    public ClsTicket setDone_button_boolean(Object done_button_boolean) {
        this.done_button_boolean = done_button_boolean;
        return this;
    }

    /**
     * Set the open_boolean
     *
     * @param open_boolean the value
     */
    public ClsTicket setOpen_boolean(Object open_boolean) {
        this.open_boolean = open_boolean;
        return this;
    }

    /**
     * Set the sh_days_to_reach
     *
     * @param sh_days_to_reach the value
     */
    public ClsTicket setSh_days_to_reach(Integer sh_days_to_reach) {
        this.sh_days_to_reach = sh_days_to_reach;
        return this;
    }

    /**
     * Set the sh_days_to_late
     *
     * @param sh_days_to_late the value
     */
    public ClsTicket setSh_days_to_late(Integer sh_days_to_late) {
        this.sh_days_to_late = sh_days_to_late;
        return this;
    }

    /**
     * Set the sh_ticket_report_url
     *
     * @param sh_ticket_report_url the value
     */
    public ClsTicket setSh_ticket_report_url(String sh_ticket_report_url) {
        this.sh_ticket_report_url = sh_ticket_report_url;
        return this;
    }

    /**
     * Set the portal_ticket_url_wp
     *
     * @param portal_ticket_url_wp the value
     */
    public ClsTicket setPortal_ticket_url_wp(String portal_ticket_url_wp) {
        this.portal_ticket_url_wp = portal_ticket_url_wp;
        return this;
    }

    /**
     * Set the email_subject
     *
     * @param email_subject the value
     */
    public ClsTicket setEmail_subject(String email_subject) {
        this.email_subject = email_subject;
        return this;
    }

    /**
     * Set the sh_sla_status_ids
     *
     * @param sh_sla_status_ids the value
     */
    public ClsTicket setSh_sla_status_ids(Object sh_sla_status_ids) {
        this.sh_sla_status_ids = sh_sla_status_ids;
        return this;
    }

    /**
     * Set the sh_display_multi_user
     *
     * @param sh_display_multi_user the value
     */
    public ClsTicket setSh_display_multi_user(Object sh_display_multi_user) {
        this.sh_display_multi_user = sh_display_multi_user;
        return this;
    }

    /**
     * Set the sh_display_product
     *
     * @param sh_display_product the value
     */
    public ClsTicket setSh_display_product(Object sh_display_product) {
        this.sh_display_product = sh_display_product;
        return this;
    }

    /**
     * Set the state
     *
     * @param state the value
     */
    public ClsTicket setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * Set the ticket_type
     *
     * @param ticket_type the value
     */
    public ClsTicket setTicket_type(Object ticket_type) {
        this.ticket_type = ticket_type;
        return this;
    }

    /**
     * Set the ticket_allocated
     *
     * @param ticket_allocated the value
     */
    public ClsTicket setTicket_allocated(Object ticket_allocated) {
        this.ticket_allocated = ticket_allocated;
        return this;
    }

    /**
     * Set the team_head
     *
     * @param team_head the value
     */
    public ClsTicket setTeam_head(Object team_head) {
        this.team_head = team_head;
        return this;
    }

    /**
     * Set the sh_user_ids
     *
     * @param sh_user_ids the value
     */
    public ClsTicket setSh_user_ids(Object sh_user_ids) {
        this.sh_user_ids = sh_user_ids;
        return this;
    }

    /**
     * Set the subject_id
     *
     * @param subject_id the value
     */
    public ClsTicket setSubject_id(Object subject_id) {
        this.subject_id = subject_id;
        return this;
    }

    /**
     * Set the sub_category_id
     *
     * @param sub_category_id the value
     */
    public ClsTicket setSub_category_id(Object sub_category_id) {
        this.sub_category_id = sub_category_id;
        return this;
    }

    /**
     * Set the tag_ids
     *
     * @param tag_ids the value
     */
    public ClsTicket setTag_ids(Object tag_ids) {
        this.tag_ids = tag_ids;
        return this;
    }

    /**
     * Set the write_date
     *
     * @param write_date the value
     */
    public ClsTicket setWrite_date(String write_date) {
        this.write_date = write_date;
        return this;
    }

    /**
     * Set the sh_due_date
     *
     * @param sh_due_date the value
     */
    public ClsTicket setSh_due_date(String sh_due_date) {
        this.sh_due_date = sh_due_date;
        return this;
    }

    /**
     * Set the person_name
     *
     * @param person_name the value
     */
    public ClsTicket setPerson_name(String person_name) {
        this.person_name = person_name;
        return this;
    }

    /**
     * Set the email
     *
     * @param email the value
     */
    public ClsTicket setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Set the mobile_no
     *
     * @param mobile_no the value
     */
    public ClsTicket setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
        return this;
    }

    /**
     * Set the replied_date
     *
     * @param replied_date the value
     */
    public ClsTicket setReplied_date(String replied_date) {
        this.replied_date = replied_date;
        return this;
    }

    /**
     * Set the task_ids
     *
     * @param task_ids the value
     */
    public ClsTicket setTask_ids(Object task_ids) {
        this.task_ids = task_ids;
        return this;
    }

    /**
     * Set the product_ids
     *
     * @param product_ids the value
     */
    public ClsTicket setProduct_ids(Object product_ids) {
        this.product_ids = product_ids;
        return this;
    }

    /**
     * Set the sh_lead_ids
     *
     * @param sh_lead_ids the value
     */
    public ClsTicket setSh_lead_ids(Object sh_lead_ids) {
        this.sh_lead_ids = sh_lead_ids;
        return this;
    }

    /**
     * Set the sh_invoice_ids
     *
     * @param sh_invoice_ids the value
     */
    public ClsTicket setSh_invoice_ids(Object sh_invoice_ids) {
        this.sh_invoice_ids = sh_invoice_ids;
        return this;
    }

    /**
     * Set the sh_purchase_order_ids
     *
     * @param sh_purchase_order_ids the value
     */
    public ClsTicket setSh_purchase_order_ids(Object sh_purchase_order_ids) {
        this.sh_purchase_order_ids = sh_purchase_order_ids;
        return this;
    }

    /**
     * Set the sh_sale_order_ids
     *
     * @param sh_sale_order_ids the value
     */
    public ClsTicket setSh_sale_order_ids(Object sh_sale_order_ids) {
        this.sh_sale_order_ids = sh_sale_order_ids;
        return this;
    }

    /**
     * Set the sh_status
     *
     * @param sh_status the value
     */
    public ClsTicket setSh_status(Object sh_status) {
        this.sh_status = sh_status;
        return this;
    }

    /**
     * Set the sh_sla_deadline
     *
     * @param sh_sla_deadline the value
     */
    public ClsTicket setSh_sla_deadline(Object sh_sla_deadline) {
        this.sh_sla_deadline = sh_sla_deadline;
        return this;
    }

    /**
     * Set the sh_sla_policy_ids
     *
     * @param sh_sla_policy_ids the value
     */
    public ClsTicket setSh_sla_policy_ids(Object sh_sla_policy_ids) {
        this.sh_sla_policy_ids = sh_sla_policy_ids;
        return this;
    }

    /**
     * Set the sh_ticket_alarm_ids
     *
     * @param sh_ticket_alarm_ids the value
     */
    public ClsTicket setSh_ticket_alarm_ids(Object sh_ticket_alarm_ids) {
        this.sh_ticket_alarm_ids = sh_ticket_alarm_ids;
        return this;
    }

    /**
     * Set the description
     *
     * @param description the value
     */
    public ClsTicket setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set the attachment_ids
     *
     * @param attachment_ids the value
     */
    public ClsTicket setAttachment_ids(Object attachment_ids) {
        this.attachment_ids = attachment_ids;
        return this;
    }

    /**
     * Set the priority_new
     *
     * @param priority_new the value
     */
    public ClsTicket setPriority_new(Object priority_new) {
        this.priority_new = priority_new;
        return this;
    }

    /**
     * Set the customer_comment
     *
     * @param customer_comment the value
     */
    public ClsTicket setCustomer_comment(Object customer_comment) {
        this.customer_comment = customer_comment;
        return this;
    }

    /**
     * Set the close_date
     *
     * @param close_date the value
     */
    public ClsTicket setClose_date(Object close_date) {
        this.close_date = close_date;
        return this;
    }

    /**
     * Set the close_by
     *
     * @param close_by the value
     */
    public ClsTicket setClose_by(Object close_by) {
        this.close_by = close_by;
        return this;
    }

    /**
     * Set the comment
     *
     * @param comment the value
     */
    public ClsTicket setComment(Object comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Set the cancel_date
     *
     * @param cancel_date the value
     */
    public ClsTicket setCancel_date(Object cancel_date) {
        this.cancel_date = cancel_date;
        return this;
    }

    /**
     * Set the cancel_by
     *
     * @param cancel_by the value
     */
    public ClsTicket setCancel_by(Object cancel_by) {
        this.cancel_by = cancel_by;
        return this;
    }

    /**
     * Set the cancel_reason
     *
     * @param cancel_reason the value
     */
    public ClsTicket setCancel_reason(Object cancel_reason) {
        this.cancel_reason = cancel_reason;
        return this;
    }

    /**
     * Set the form_url
     *
     * @param form_url the value
     */
    public ClsTicket setForm_url(String form_url) {
        this.form_url = form_url;
        return this;
    }

    /**
     * Set the message_follower_ids
     *
     * @param message_follower_ids the value
     */
    public ClsTicket setMessage_follower_ids(Object message_follower_ids) {
        this.message_follower_ids = message_follower_ids;
        return this;
    }

    /**
     * Set the message_ids
     *
     * @param message_ids the value
     */
    public ClsTicket setMessage_ids(Object message_ids) {
        this.message_ids = message_ids;
        return this;
    }

    /**
     * Set the activity_ids
     *
     * @param activity_ids the value
     */
    public ClsTicket setActivity_ids(Object activity_ids) {
        this.activity_ids = activity_ids;
        return this;
    }

    /**
     * Set the access_token
     *
     * @param access_token the value
     */
    public ClsTicket setAccess_token(String access_token) {
        this.access_token = access_token;
        return this;
    }

    /**
     * Set the report_token
     *
     * @param report_token the value
     */
    public ClsTicket setReport_token(String report_token) {
        this.report_token = report_token;
        return this;
    }

    /**
     * Set the stage
     *
     * @param stage the value
     */
    public ClsTicket setStage(ClsStage stage) {
        this.stage = stage;
        return this;
    }

    /**
     * Set the category
     *
     * @param category the value
     */
    public ClsTicket setCategory(ClsCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Set the categorySub
     *
     * @param categorySub the value
     */
    public ClsTicket setCategorySub(ClsCategorySub categorySub) {
        this.categorySub = categorySub;
        return this;
    }

    /**
     * Set the helpdeskTeam
     *
     * @param helpdeskTeam the value
     */
    public ClsTicket setHelpdeskTeam(ClsHelpdeskTeam helpdeskTeam) {
        this.helpdeskTeam = helpdeskTeam;
        return this;
    }

    /**
     * Set the clsTeamHead
     *
     * @param clsTeamHead the value
     */
    public ClsTicket setClsTeamHead(ClsTeamHead clsTeamHead) {
        this.clsTeamHead = clsTeamHead;
        return this;
    }

    /**
     * Set the subjectType
     *
     * @param subjectType the value
     */
    public ClsTicket setSubjectType(ClsSubjectType subjectType) {
        this.subjectType = subjectType;
        return this;
    }

    /**
     * Set the ticketType
     *
     * @param ticketType the value
     */
    public ClsTicket setTicketType(ClsTicketType ticketType) {
        this.ticketType = ticketType;
        return this;
    }

    /**
     * Set the partner
     *
     * @param partner the value
     */
    public ClsTicket setPartner(ClsPartner partner) {
        this.partner = partner;
        return this;
    }

    //    public Ticket copyTo(Ticket ticket) {
//        ClsTicket cls = this;
//        ticket.setOdStage(cls.getStage());
//        ticket.setTicketNumber(cls.getId());
//        ticket.setTicketText(cls.getName());
//        ticket.setStatus(convertStatus());
//        ticket.setDownloadUrl(cls.getSh_ticket_report_url());
//        ticket.setViewUrl(cls.getPortal_ticket_url_wp());
//        ticket.setFormUrl(cls.getForm_url());
//        ticket.setCloseOn(OdooHelper.dateTime(cls.getClose_date()));
//        ticket.setCloseBy(OdooHelper.toString(cls.getClose_by()));
//        ticket.setCancelOn(OdooHelper.dateTime(cls.getCancel_date()));
//        ticket.setCancelReason(OdooHelper.toString(cls.getCancel_reason()));
//        ticket.getCustomField().set("access_token", cls.getAccess_token())
//                .set("report_token", cls.getReport_token());
//
//        return ticket;
//    }

//    public Ticket createNew() {
//        Ticket ticket = new Ticket();
//        ClsTicket cls = this;
//        ticket.setOdStage(getOrNewStage());
//        ticket.setOdCategory(cls.getCategory());
//        ticket.setOdCateSub(cls.getCategorySub());
//        ticket.setOdSubjectType(cls.getSubjectType());
//        ticket.setOdTicketType(cls.getTicketType());
//        ticket.setOdPartner(cls.getPartner());
//        ticket.setOdTeam(cls.getHelpdeskTeam());
//        ticket.setOdTeamHead(cls.getClsTeamHead());
//        ticket.setCustomerName(cls.getPerson_name());
//        //ticket.setFullName(clsUser.getDisplay_name());
//        ticket.setPhone(cls.getMobile_no());
//        ticket.setOdRepiledStatus(ClsRepiledStatus.from(cls.getState()));
//
//        ticket.setTicketNumber(cls.getId());
//        ticket.setTicketText(cls.getName());
//        ticket.setStatus(convertStatus());
//        ticket.setBody(cls.getDescription());
//        ticket.setSubject(cls.getEmail_subject());
//        ticket.setEmail(cls.getEmail());
//        ticket.setDownloadUrl(cls.getSh_ticket_report_url());
//        ticket.setViewUrl(cls.getPortal_ticket_url_wp());
//        ticket.setFormUrl(cls.getForm_url());
//        ticket.setCloseOn(OdooHelper.dateTime(cls.getClose_date()));
//        ticket.setCloseBy(OdooHelper.toString(cls.getClose_by()));
//        ticket.setCancelOn(OdooHelper.dateTime(cls.getCancel_date()));
//        ticket.setCancelReason(OdooHelper.toString(cls.getCancel_reason()));
//        ticket.getCustomField().set("access_token", cls.getAccess_token())
//                .set("report_token", cls.getReport_token());
//
//        return ticket;
//    }

//    private Ticket.Status convertStatus() {
//        return stage == null ? Ticket.Status.TaoMoi : stage.convertStatus();
//    }


    public LocalDateTime getCreateAt() {
        return ClsHelper.dateTime(getCreate_date());
    }

    public LocalDateTime getWriteAt() {
        return ClsHelper.dateTime(getWrite_date());
    }


    public Map<String, Object> validateCreate() {
        Map<String, Object> errorList = new LinkedHashMap<>();
        if (Objects.isEmpty(state)) errorList.put("od_repiled_status", "");
        if (Objects.isNull(team_id)) errorList.put("od_team", "");
        if (Objects.isNull(user_id)) errorList.put("od_assign", "");
        if (Objects.isNull(team_head)) errorList.put("od_team_head", "");
        if (Objects.isNull(category_id)) errorList.put("od_category", "");
        if (Objects.isNull(sub_category_id)) errorList.put("od_category_sub", "");
        if (Objects.isNull(subject_id)) errorList.put("od_subject_type", "");
        return errorList;
    }

    public ClsTicket updateValueFrom(ClsTicket other) {
        return other;
    }
}