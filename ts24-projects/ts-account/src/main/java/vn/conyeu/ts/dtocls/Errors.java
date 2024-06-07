package vn.conyeu.ts.dtocls;

import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.ts.ticket_rest.TicketAction;

import java.util.List;

public final class Errors {

    public static BaseException noUserApiCode(String apiCode) {
        return new NotFound("user_api.code_404")
                .message("Khong tim thay user api [%s]", apiCode)
                .detail("apiCode", apiCode);
    }

    public static BaseException notPartnerId(Long partnerId) {
        return new NotFound("od_partner.uid_404")
                .message("Khong tim thay partner [%s]", partnerId)
                .detail("uid", partnerId);
    }

    public static BaseException noOdUserId(Long userId) {
        return new NotFound("od_user.uid_404")
                .message("Khong tim thay user [%s]", userId)
                .detail("uid", userId);
    }

    public static BaseException odTicketNotCreate(Long ticketID, String suffixMsg) {
        String message = "Ticket `id=%s` trên hệ thống chưa được gửi";
        if(suffixMsg != null) message = message + " -> " + suffixMsg;
        return new NotFound("od_ticket.uid_404").detail("uid", ticketID)
                .message(message, ticketID, suffixMsg);
    }

    public static BaseException odTicketHasSend(Long ticketId) {
        return BaseException.e400("ticket_exist").detail("ticket_id", ticketId)
                .message("[%s] Ticket đã tạo rồi -> Không thể tạo nữa.", ticketId);
    }

    public static BaseException odTicketNotCreate(Long ticketID) {
        return odTicketNotCreate(ticketID, null);
    }

    public static BaseException noOdTicketId(Long ticketNumber) {
        return new NotFound("od_ticket.uid_404")
                .message("Ticket `id=%s` không tồn tại trên hệ thống api", ticketNumber)
                .detail("uid", ticketNumber);
    }

    public static BaseException invalidAction(TicketAction action) {
        return BaseException.e500("invalid_action").detail("action", action)
                .message("Không tìm thấy ticket_action = %s", action)
                .detail("actions", TicketAction.values());
    }


    public static BaseException odNoteHasCreate(Long ticketNum, Long noteId) {
        return BaseException.e400("note_create")
                .message("[%s - %s] Ticket này đã tạo ghi chú rồi -> Không thể tạo nữa.", ticketNum, noteId)
                .detail("ticket_number", ticketNum).detail("note_id", noteId);
    }

    public static BaseException invalidImageAttach(Long ticketId, List<String> allFile) {
        return BaseException.e400("no_file").detail("images", allFile)
                .message("Ticket `%s` chưa đính kèm đầy đủ tệp -> [%s]",
                        ticketId, String.join(",", allFile));
    }

    public static BaseException noEmailTicket(Long ticketId) {
        return BaseException.e400("invalid_ticket")
                .message("[%s] Ticket này không phải là email ticket", ticketId);
    }
}