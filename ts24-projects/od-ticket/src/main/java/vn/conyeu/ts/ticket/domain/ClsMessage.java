package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import org.springframework.util.Assert;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsHelper;
import vn.conyeu.ts.odcore.domain.ClsModel;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
public class ClsMessage extends ClsModel<ClsMessage> {
    //List<Object> partner_ids = new LinkedList<>();
    //List<Object> attachment_ids = new LinkedList<>();
    //List<Object> canned_response_ids = new LinkedList<>();

    Long id;
    String body;
    LocalDateTime date;
    Object author_id;
    String email_from;
    String message_type;
    Object subtype_id;
    String subject;
    String model;
    Long res_id;
    String record_name;
    Object partner_ids;
    Object starred_partner_ids;
    Object notifications;
    Object attachment_ids;
    Object tracking_value_ids;
    Object messageReactionGroups;
    Object needaction_partner_ids;
    Object history_partner_ids;
    Boolean is_note;
    Boolean is_discussion;
    Boolean subtype_description;
    Boolean is_notification;
    String subtype_xmlid;

    ObjectMap attachObj;
    ClsFileMap fileMap;

    /**
     * Returns the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id the value
     */
    public ClsMessage setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Returns the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Set the body
     *
     * @param body the value
     */
    public ClsMessage setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Returns the date
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Set the date
     *
     * @param date the value
     */
    public ClsMessage setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    /**
     * Returns the author_id
     */
    public Object getAuthor_id() {
        return author_id;
    }

    /**
     * Set the author_id
     *
     * @param author_id the value
     */
    public ClsMessage setAuthor_id(Object author_id) {
        this.author_id = author_id;
        return this;
    }

    /**
     * Returns the email_from
     */
    public String getEmail_from() {
        return email_from;
    }

    /**
     * Set the email_from
     *
     * @param email_from the value
     */
    public ClsMessage setEmail_from(String email_from) {
        this.email_from = email_from;
        return this;
    }

    /**
     * Returns the message_type
     */
    public String getMessage_type() {
        return message_type;
    }

    /**
     * Set the message_type
     *
     * @param message_type the value
     */
    public ClsMessage setMessage_type(String message_type) {
        this.message_type = message_type;
        return this;
    }

    /**
     * Returns the subtype_id
     */
    public Object getSubtype_id() {
        return subtype_id;
    }

    /**
     * Set the subtype_id
     *
     * @param subtype_id the value
     */
    public ClsMessage setSubtype_id(Object subtype_id) {
        this.subtype_id = subtype_id;
        return this;
    }

    /**
     * Returns the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the subject
     *
     * @param subject the value
     */
    public ClsMessage setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Returns the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Set the model
     *
     * @param model the value
     */
    public ClsMessage setModel(String model) {
        this.model = model;
        return this;
    }

    /**
     * Returns the res_id
     */
    public Long getRes_id() {
        return res_id;
    }

    /**
     * Set the res_id
     *
     * @param res_id the value
     */
    public ClsMessage setRes_id(Long res_id) {
        this.res_id = res_id;
        return this;
    }

    /**
     * Returns the record_name
     */
    public String getRecord_name() {
        return record_name;
    }

    /**
     * Set the record_name
     *
     * @param record_name the value
     */
    public ClsMessage setRecord_name(String record_name) {
        this.record_name = record_name;
        return this;
    }

    /**
     * Returns the partner_ids
     */
    public Object getPartner_ids() {
        return partner_ids;
    }

    /**
     * Set the partner_ids
     *
     * @param partner_ids the value
     */
    public ClsMessage setPartner_ids(Object partner_ids) {
        this.partner_ids = partner_ids;
        return this;
    }

    /**
     * Returns the starred_partner_ids
     */
    public Object getStarred_partner_ids() {
        return starred_partner_ids;
    }

    /**
     * Set the starred_partner_ids
     *
     * @param starred_partner_ids the value
     */
    public ClsMessage setStarred_partner_ids(Object starred_partner_ids) {
        this.starred_partner_ids = starred_partner_ids;
        return this;
    }

    /**
     * Returns the notifications
     */
    public Object getNotifications() {
        return notifications;
    }

    /**
     * Set the notifications
     *
     * @param notifications the value
     */
    public ClsMessage setNotifications(Object notifications) {
        this.notifications = notifications;
        return this;
    }

    /**
     * Returns the attachment_ids
     */
    public Object getAttachment_ids() {
        return attachment_ids;
    }

    /**
     * Set the attachment_ids
     *
     * @param attachment_ids the value
     */
    public ClsMessage setAttachment_ids(Object attachment_ids) {
        this.attachment_ids = attachment_ids;
        return this;
    }

    /**
     * Returns the tracking_value_ids
     */
    public Object getTracking_value_ids() {
        return tracking_value_ids;
    }

    /**
     * Set the tracking_value_ids
     *
     * @param tracking_value_ids the value
     */
    public ClsMessage setTracking_value_ids(Object tracking_value_ids) {
        this.tracking_value_ids = tracking_value_ids;
        return this;
    }

    /**
     * Returns the messageReactionGroups
     */
    public Object getMessageReactionGroups() {
        return messageReactionGroups;
    }

    /**
     * Set the messageReactionGroups
     *
     * @param messageReactionGroups the value
     */
    public ClsMessage setMessageReactionGroups(Object messageReactionGroups) {
        this.messageReactionGroups = messageReactionGroups;
        return this;
    }

    /**
     * Returns the needaction_partner_ids
     */
    public Object getNeedaction_partner_ids() {
        return needaction_partner_ids;
    }

    /**
     * Set the needaction_partner_ids
     *
     * @param needaction_partner_ids the value
     */
    public ClsMessage setNeedaction_partner_ids(Object needaction_partner_ids) {
        this.needaction_partner_ids = needaction_partner_ids;
        return this;
    }

    /**
     * Returns the history_partner_ids
     */
    public Object getHistory_partner_ids() {
        return history_partner_ids;
    }

    /**
     * Set the history_partner_ids
     *
     * @param history_partner_ids the value
     */
    public ClsMessage setHistory_partner_ids(Object history_partner_ids) {
        this.history_partner_ids = history_partner_ids;
        return this;
    }

    /**
     * Returns the is_note
     */
    public Boolean getIs_note() {
        return is_note;
    }

    /**
     * Set the is_note
     *
     * @param is_note the value
     */
    public ClsMessage setIs_note(Boolean is_note) {
        this.is_note = is_note;
        return this;
    }

    /**
     * Returns the is_discussion
     */
    public Boolean getIs_discussion() {
        return is_discussion;
    }

    /**
     * Set the is_discussion
     *
     * @param is_discussion the value
     */
    public ClsMessage setIs_discussion(Boolean is_discussion) {
        this.is_discussion = is_discussion;
        return this;
    }

    /**
     * Returns the subtype_description
     */
    public Boolean getSubtype_description() {
        return subtype_description;
    }

    /**
     * Set the subtype_description
     *
     * @param subtype_description the value
     */
    public ClsMessage setSubtype_description(Boolean subtype_description) {
        this.subtype_description = subtype_description;
        return this;
    }

    /**
     * Returns the is_notification
     */
    public Boolean getIs_notification() {
        return is_notification;
    }

    /**
     * Set the is_notification
     *
     * @param is_notification the value
     */
    public ClsMessage setIs_notification(Boolean is_notification) {
        this.is_notification = is_notification;
        return this;
    }

    /**
     * Returns the subtype_xmlid
     */
    public String getSubtype_xmlid() {
        return subtype_xmlid;
    }

    /**
     * Set the subtype_xmlid
     *
     * @param subtype_xmlid the value
     */
    public ClsMessage setSubtype_xmlid(String subtype_xmlid) {
        this.subtype_xmlid = subtype_xmlid;
        return this;
    }

    /**
     * Returns the attachObj
     */
    public ObjectMap getAttachObj() {
        return attachObj;
    }

    /**
     * Set the attachObj
     *
     * @param attachObj the value
     */
    public ClsMessage setAttachObj(ObjectMap attachObj) {
        this.attachObj = attachObj;
        return this;
    }

    /**
     * Returns the fileMap
     */
    public ClsFileMap getFileMap() {
        return fileMap;
    }

    /**
     * Set the fileMap
     *
     * @param fileMap the value
     */
    public ClsMessage setFileMap(ClsFileMap fileMap) {
        this.fileMap = fileMap;
        return this;
    }

    public void setDate(String date) {
        this.date = ClsHelper.dateTime(date);
    }

    public ObjectMap attachObj() {
        return attachObj = ObjectMap.ifNull(attachObj);
    }

    public ClsMessage addFile(String fileName, byte[] data) {
        attachObj().set(fileName, data);
        return this;
    }

    public ObjectMap cloneMap() {
        ObjectMap newMap = ObjectMap.fromJson(this);
        newMap.remove("attachObj");
        return newMap;
    }

    public ClsMessage validateSend() {
        Assert.hasLength(getModel(), "@thread_model");
        Assert.notNull(getRes_id(), "@thread_id");
        boolean hasErr = Objects.isNull(body) && Objects.isNull(attachment_ids);
        if(hasErr) throw new IllegalArgumentException("body || attachment_ids");
        return this;
    }

    public static ClsMessage create() {
        ClsMessage clsNote = new ClsMessage();
        clsNote.partner_ids = new Object[0];
        clsNote.subtype_xmlid = "mail.mt_note";
        clsNote.message_type = "comment";
        return clsNote;
    }

    public static ClsMessage forTicket(Long ticketId) {
       return create().setModel("helpdesk.ticket", ticketId);
    }

//    public static ClsMessage from(Ticket ticket) {
//        ClsMessage clsNote = create();
//        clsNote.body = ticket.getNoteHtml();
//        clsNote.attachment_ids = ticket.getImageIds();
//        clsNote.setId(ticket.getNoteId());
//        return clsNote;
//    }

    public static ClsMessage from(ObjectMap map) {
        ClsMessage clsNote = map.asObject(ClsMessage.class);
        clsNote.setDate(map.getString("date"));
        return clsNote;
    }

    public ClsMessage id(Long noteId) {
        setId(noteId);
        return this;
    }

    public ClsMessage setModel(String threadModel, Long res_id) {
        this.setModel(threadModel);
        this.setRes_id(res_id);
        return this;
    }

    @Override
    public ClsMessage updateFromMap(ObjectMap mapData) {
        super.updateFromMap(mapData);
        return this;
    }

    public ObjectMap cloneCreate() {
        return ObjectMap.create()
                .set("body", Objects.firstNotNull(getBody(), ""))
                .set("attachment_ids", Objects.firstNotNull(getAttachment_ids(), new Object[0]))
                .set("message_type", Objects.firstNotNull(getMessage_type(), "comment"))
                .set("subtype_xmlid", Objects.firstNotNull(getSubtype_xmlid(), "mail.mt_note"))
                .set("partner_ids", Objects.firstNotNull(getPartner_ids(),new Object[0]));

    }

}