package vn.conyeu.ts.domain;

public enum TicketStatus {
//    NO_SEND("Chưa gửi"),
//    NEW("Tạo mới"),
//    PROCESSING("Đang xử lý"),
//    COMPLETE("Hoàn thành"),
//    CLOSED("Đã đóng"),
//    CANCEL("Hủy"),
//    OPENING("Mở lại"),
//    DELETE("Đã xóa");
    NEW("Tạo mới"),
    SEND("Đã gửi"),
    NOTE("Đã tạo note"),
    ATTACH("Đã đính kèm hình"),
    CLOSE("Đã đóng"),
    EMAIL("Đã gửi email"),
    DELETE("Đã xóa")

    ;

    final String text;
    TicketStatus(String text) {
        this.text = text;
    }
}