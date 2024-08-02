package vn.conyeu.ts.ticket_rest;

import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.domain.TicketDetail;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.ticket.domain.*;

import java.util.List;

public final class OdTRHelper {

//    public static TicketStatus fromStage(ClsStage stage) {
//        if (stage == null) return TicketStatus.NEW;
//        else {
//            String name = stage.getName();
//            if ("Mới".equals(name)) return TicketStatus.NEW;
//            if ("Đang xử lý".equals(name)) return TicketStatus.PROCESSING;
//            if ("Hoàn thành".equals(name)) return TicketStatus.COMPLETE;
//            if ("Đã đóng".equals(name)) return TicketStatus.CLOSED;
//            if ("Mở lại".equals(name)) return TicketStatus.OPENING;
//            if ("Hủy".equals(name)) return TicketStatus.CANCEL;
//            else throw new UnsupportedOperationException(name);
//        }
//    }

    public static Ticket fromClsTicket(ClsTicket cls) {
        Ticket ticket = new Ticket();
        ticket.setBody(cls.getDescription());
        ticket.setSubject(cls.getEmail_subject());
        ticket.setEmail(cls.getEmail());
        ticket.setOdCate(cls.getCategory());
        ticket.setOdCateSub(cls.getCategorySub());
        ticket.setOdSubjectType(cls.getSubjectType());
        ticket.setOdTicketType(cls.getTicketType());
        ticket.setOdPartner(cls.getPartner());
        ticket.setOdTeam(cls.getHelpdeskTeam());
        ticket.setOdTeamHead(cls.getClsTeamHead());
        ticket.setCustomerName(cls.getPerson_name());
        ticket.setPhone(cls.getMobile_no());
        ticket.setStage(cls.getStage());
        ticket.setAccessToken(cls.getAccess_token());
        ticket.setReportToken(cls.getReport_token());

        TicketDetail detail = ticket.getDetail();
        detail.setTicketNumber(cls.getId());
        detail.setTicketText(cls.getName());
        detail.setDownloadUrl(cls.getSh_ticket_report_url());
        detail.setViewUrl(cls.getPortal_ticket_url_wp());
        detail.setFormUrl(cls.getForm_url());
        detail.setCancelReason(cls.getCancel_reason());
        detail.setCancelAt(cls.getCancelDateTime());
        detail.setClosedAt(cls.getCloseDateTime());
        detail.setCloseBy(cls.getClose_byId());

        //ticket.setFullName(clsUser.getDisplay_name());
        //ticket.setOdRepiledStatus(ClsRepliedStatus.from(cls.getState()));
        return ticket;
    }

    public static Ticket updateTicket(Ticket ticket, ClsTicket cls) {
        ticket.setStage(cls.getStage());
        ticket.setAccessToken(cls.getAccess_token());
        ticket.setReportToken(cls.getReport_token());

        TicketDetail detail = ticket.getDetail();
        detail.setTicketNumber(cls.getId());
        detail.setTicketText(cls.getName());
        detail.setDownloadUrl(cls.getSh_ticket_report_url());
        detail.setViewUrl(cls.getPortal_ticket_url_wp());
        detail.setFormUrl(cls.getForm_url());
        detail.setClosedAt(cls.getCloseDateTime());
        detail.setCloseBy(cls.getClose_byId());
        detail.setCancelAt(cls.getCancelDateTime());
        detail.setCancelReason(cls.getCancel_reason());

        return ticket;
    }

    public static ClsTicket fromTicket(Ticket ticket) {
       // TicketDetail detail = ticket.getDetail();
        ClsTicket cls = new ClsTicket();

        cls.setStage_id(4);
        cls.setActive(true);
        cls.setTicket_from_website(false);
        cls.setTicket_from_portal(false);
        cls.setEmail_subject(ticket.getSubject());
        cls.setSh_sla_status_ids(new Object[0]);
        cls.setTicket_allocated(false);

        ClsRepliedStatus repliedStatus = ticket.getOdReplied();
        if(repliedStatus != null) cls.setState(repliedStatus.getCode());

        ClsTicketType ticketType = ticket.getOdTicketType();
        if(ticketType != null) cls.setTicket_type(ticketType.getId());

        ClsUser clsUser = ticket.getOdAssign();
        if(clsUser != null) cls.setUser_id(clsUser.getId());

        ClsSubjectType subType = ticket.getOdSubjectType();
        if(subType != null) cls.setSubject_id(subType.getId());

        ClsCategory clsCategory = ticket.getOdCate();
        if(clsCategory != null) cls.setCategory_id(clsCategory.getId());

        ClsCategorySub clsCategorySub = ticket.getOdCateSub();
        if(clsCategorySub != null) cls.setSub_category_id(clsCategorySub.getId());

        ClsTicketPriority clsTicketPriority = ticket.getOdPriority();
        if(clsTicketPriority != null) cls.setPriority(clsTicketPriority.getId());

        List<ClsTicketTag> clsTicketTags = ticket.getOdTags();
        if(clsTicketTags != null && !clsTicketTags.isEmpty()) {
            Object[] tagsId = clsTicketTags.stream().map(ClsTicketTag::getId).toArray();
            cls.setTag_ids(createList(6, false, tagsId));
        }

        ClsHelpdeskTeam clsTeam = ticket.getOdTeam();
        if(clsTeam != null)cls.setTeam_id(clsTeam.getId());

        ClsTeamHead clsTeamHead = ticket.getOdTeamHead();
        if(clsTeamHead != null)cls.setTeam_head(clsTeamHead.getId());

        cls.setPartner_id(ticket.getOdPartnerId());
        cls.setPerson_name(ticket.getCustomerName());
        cls.setEmail(ticket.getEmail());
        cls.setMobile_no(ticket.getPhone());
        cls.setSh_status(false);

        //không tạo body 10/03/2023 -> thêm body vào note
        //cls.setDescription(detail.getBodyHtml());

        cls.setPriority_new(false);
        cls.setCustomer_comment(false);
        cls.setClose_date(false);
        cls.setClose_by(false);
        cls.setComment(false);
        cls.setCancel_date(false);
        cls.setCancel_reason(false);
        cls.setMessage_follower_ids(new Object[0]);
        cls.setMessage_ids(new Object[0]);
        cls.setActivity_ids(new Object[0]);

        cls.setSh_user_ids(createList(6, false, new Object[0]));
        cls.setTask_ids(createList(6, false, new Object[0]));
        cls.setProduct_ids(createList(6, false, new Object[0]));
        cls.setSh_lead_ids(createList(6, false, new Object[0]));
        cls.setSh_invoice_ids(createList(6, false, new Object[0]));
        cls.setSh_purchase_order_ids(createList(6, false, new Object[0]));
        cls.setSh_sale_order_ids(createList(6, false, new Object[0]));
        cls.setSh_sla_policy_ids(createList(6, false, new Object[0]));
        cls.setSh_ticket_alarm_ids(createList(6, false, new Object[0]));
        cls.setAttachment_ids(createList(6, false, new Object[0]));

        return cls;
    }

    public static Object[] createList(Object...args) {
        return new Object[] {args};
    }

}