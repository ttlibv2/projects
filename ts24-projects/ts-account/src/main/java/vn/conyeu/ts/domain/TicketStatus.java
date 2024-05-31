package vn.conyeu.ts.domain;

public enum TicketStatus {
    NEW("Tạo mới"),
    PROCESSING("Đang xử lý"),
    COMPLETE("Hoàn thành"),
    CLOSED("Đã đóng"),
    CANCEL("Hủy"),
    OPENING("Mở lại");

    final String text;
    TicketStatus(String text) {
        this.text = text;
    }
}