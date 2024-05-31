package vn.conyeu.ts.ticket.service;

import lombok.extern.slf4j.Slf4j;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.odcore.domain.ClsPage;
import vn.conyeu.ts.odcore.domain.ClsUserContext;
import vn.conyeu.ts.ticket.domain.ClsFollow;
import vn.conyeu.ts.ticket.domain.ClsMessage;
import vn.conyeu.ts.ticket.domain.ClsTicket;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class OdTicket extends OdTicketCore<ClsTicket> {
    public static final String MODEL = "helpdesk.ticket";
    protected final OdMessage odMessage;
    protected final OdFollow odMail;

    public OdTicket(ClsApiConfig apiConfig, OdMessage odMessage, OdFollow odMail) {
        super(apiConfig);
        this.odMessage = odMessage;
        this.odMail = odMail;
    }

    public String getModel() {
        return MODEL;
    }
    public String getBasePath() {
        return "call_kw/helpdesk.ticket";
    }
    protected Class<ClsTicket> getDomainCls() {
        return ClsTicket.class;
    }
    protected Function<ObjectMap, ClsTicket> mapToObject() {
        return ClsTicket::from;
    }

    @Override
    protected ClsUserContext createUserContext() {
        return super.createUserContext()
                .set("allowed_company_ids", 1);
    }

    /**
     * Find ticket from web by id
     *
     * @param ticketID Long
     */
    public ClsTicket getByID(Long ticketID) {
        List<ClsTicket> all = read(Collections.singletonList(ticketID));

        if (all.isEmpty()) {
            throw BaseException.e404("no_id")
                    .message("Đã xảy ra lỗi lấy thông tin ticket số [%s] trên hệ thống helpdesk !!", ticketID)
                    .arguments("id", ticketID);
        }

        return all.get(0);
    }

    /**
     * Create New Ticket
     *
     * @param clsTicket `ClsTicket`
     */
    public ClsTicket createNew(ClsTicket clsTicket) {
       // ClsTicket clsTicket = ClsTicket.fromV15(ticket);
        Map<String, Object> errorList = clsTicket.validateCreate();
        if(!errorList.isEmpty()) {
            throw BaseException.e400("miss_param")
                    .message("Ticket chưa điền đầy đủ thông tin")
                    .arguments(errorList);
        }

        ObjectMap data = createAndReturnMap(clsTicket.cloneMap());
        return getByID(data.getLong("result"));
    }

    /**
     * Action close ticket
     *
     * @param ticketID Long
     */
    public ClsTicket closeTicket(Long ticketID) {
        Asserts.notNull(ticketID, "@ticketNumber");
        callButton("action_closed", new Object[]{ticketID});
        return getByID(ticketID);
    }

    /**
     * Create note for ticket
     * @param ticketId Long
     * @param noteHtml String
     * */
    public ClsMessage createNote(Long ticketId, String noteHtml) {
        Asserts.notNull(noteHtml, "@noteHtml");
        Asserts.notNull(ticketId, "@ticketId");
        ClsMessage clsMessage = ClsMessage.forTicket(ticketId).setBody(noteHtml);
        return odMessage.create(clsMessage);
    }

    /**
     * Create note for attach file
     * @param ticketId Long
     * @param fileObj ObjectMap
     * */
    public ClsMessage createNote(Long ticketId, ObjectMap fileObj) {
        Asserts.notNull(ticketId, "@ticketId");
        Asserts.notEmpty(fileObj, "@fileObj");
        ClsMessage clsMessage = ClsMessage.forTicket(ticketId);
        clsMessage.setAttachObj(fileObj);
        return odMessage.create(clsMessage);
    }

    /**
     * Create note with add file
     * @param ticketId Long
     * @param noteHtml String
     * @param fileObj ObjectMap
     * */
    public ClsMessage createNote(Long ticketId, String noteHtml, ObjectMap fileObj) {
        Asserts.notNull(ticketId, "@ticketId");
        Asserts.notEmpty(fileObj, "@fileObj");
        Asserts.notNull(noteHtml, "@noteHtml");
        return odMessage.create(ClsMessage
                .forTicket(ticketId)
                .setAttachObj(fileObj)
                .setBody(noteHtml));
    }

    public ClsMessage createNote(Long ticketNum, ClsMessage clsMsg) {
        return odMessage.create(clsMsg.setModel(getModel(), ticketNum));
    }

    public List<ClsTicket> searchTicket(ObjectMap clone, ClsPage clsPage) {
        throw new UnsupportedOperationException();
    }

    public ClsTicket updateTicket(Long ticketNumber, ClsTicket ticket) {
        ClsTicket clsTicket = getByID(ticketNumber);
        clsTicket = clsTicket.updateValueFrom(ticket);

        Map<String, Object> errorList = clsTicket.validateCreate();
        if(!errorList.isEmpty()) throw BaseException.e400("miss_param")
                .message("Ticket chưa điền đầy đủ thông tin")
                .arguments(errorList);

        Long ticketNum = updateAndReturnMap(ticketNumber, clsTicket.cloneMap())
                .getLong("id");

        return getByID(ticketNum);
    }

    public List<Long> deleteFollow(Long ticketId) {
        Long odUserId = apiConfig.getUserId();
        return deleteFollow(ticketId, odUserId);
    }

    public List<Long> deleteFollow(Long ticketId, Long odUserId) {
        List<ClsFollow> follows = odMail.read_followers(ticketId, getModel());
        List<Long> partnerId = follows.stream()
                .filter(cls -> !Objects.equals(odUserId, cls.getUserId()))
                .map(ClsFollow::getPartner_id).toList();

        if(!partnerId.isEmpty()) {
            deleteFollow(ticketId, partnerId);
        }

        return partnerId;
    }

    public  void deleteFollow(Long ticketId, List<Long> partnerId) {
        String url = String.format("%s/message_unsubscribe", getBasePath());
        ObjectMap context = ObjectMap.setNew("context", createUserContext());

        ObjectMap object = ObjectMap.create()
                .set("model", getModel())
                .set("method", "message_unsubscribe")
                .set("kwargs", context)
                .set("args", new Object[]{
                        new Object[]{ticketId},
                        partnerId
                });

        sendPost(datasetUri(url), object);
    }

}