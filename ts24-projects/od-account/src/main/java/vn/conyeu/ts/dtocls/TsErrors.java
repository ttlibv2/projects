package vn.conyeu.ts.dtocls;

import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.common.exception.Unauthorized;
import vn.conyeu.ts.restapi.odrest.TicketAction;

import java.util.Collection;

public final class TsErrors {

    public static BaseException noUserApi_AppName(String name) {
        return new BadRequest("app_name.404")
                .message("Bạn chưa cấu hình tài khoản API 'app_name=%s'", name)
                .detail("app_name", name);
    }

    public static BaseException noApiUser(Long userId, String appName) {
        return new Unauthorized("ts_api").detail("ts_api", appName)
                .detail("app_name", appName).detail("user_id", userId)
                .message("Bạn chưa cấu hình tài khoản kết nối hệ thống");
    }

    public static BaseException noUserApiByAPIID(Long apiId) {
        return new BadRequest("api_id.404")
                .message("Bạn chưa cấu hình tài khoản API 'api_id=%s'", apiId)
                .detail("api_id", apiId);
    }


    public static BaseException noAppName(String name) {
        return new NotFound().code("app_name.404")
                .detail("app_name", name)
                .message("Thông tin ứng dụng '%s' không tồn tại", name);
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

    public static BaseException invalidImageAttach(Long ticketId, Collection<String> allFile) {
        return BaseException.e400("no_file").detail("images", allFile)
                .message("Ticket `%s` chưa đính kèm đầy đủ tệp -> [%s]",
                        ticketId, String.join(",", allFile));
    }

    public static BaseException noEmailTicket(Long ticketId) {
        return BaseException.e400("invalid_ticket")
                .message("[%s] Ticket này không phải là email ticket", ticketId);
    }




}