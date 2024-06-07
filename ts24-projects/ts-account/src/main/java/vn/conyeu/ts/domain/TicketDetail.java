package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;

import java.time.LocalDateTime;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"ticket"})
@AttributeOverride(name = "id", column = @Column(name = "id"))
//@formatter:on
public class TicketDetail extends LongUId<TicketDetail> {

    @Column(length = 300)
	@JsonProperty("download_url")
    private String downloadUrl;

    @Column(length = 300)
	@JsonProperty("view_url")
    private String viewUrl;

    @Column(length = 300)
	@JsonProperty("form_url")
    private String formUrl;

    @MapsId @OneToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "ticketId")
    private Ticket ticket;

    @Column(length = 20)
	@JsonProperty("ticket_number")
    private Long ticketNumber;

    @Column(length = 100)
	@JsonProperty("ticket_text")
    private String ticketText;

    @Column(length = 20)
	@JsonProperty("note_id")
    private Long noteId;

    @Column(length = 20)
	@JsonProperty("reply_id")
    private Long replyId;

    @Column(length = 1000)
	@JsonProperty("cancel_reason")
    private String cancelReason;

    @Column(length = 100)
	@JsonProperty("stage_text")
    private String stageText;

    @Column(length = 20)
	@JsonProperty("stage_id")
    private Long stageId;

    @Column(length = 20)
	@JsonProperty("close_by")
    private Long closeBy;

    @Column(length = 20)
	@JsonProperty("mail_id")
    private Long mailId;

    @ColumnDefault("0")
    @JsonProperty("is_report")
    @Column(name = "isReport")
    private Boolean report;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
	@JsonProperty("reply_at")
    private LocalDateTime replyAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
	@JsonProperty("send_at")
    private LocalDateTime sendAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("modify_at")
    private LocalDateTime modifyAt;

	@JsonProperty("note_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime noteAt;

	@JsonProperty("close_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime closedAt;

	@JsonProperty("delete_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime deleteAt;

	@JsonProperty("image_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime imageAt;

	@JsonProperty("cancel_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime cancelAt;

	@JsonProperty("mail_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime mailAt;

}