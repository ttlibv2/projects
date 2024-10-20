package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.ticket.domain.ClsStage;

import java.time.LocalDateTime;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"ticket"})
@AttributeOverride(name = "id", column = @Column(name = "id"))
//@formatter:on
public class TicketDetail extends LongUId<TicketDetail> {

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketId")
    private Ticket ticket;

    @Column(length = 300)
    @JsonProperty("download_url")
    private String downloadUrl;

    @Column(length = 300)
    @JsonProperty("view_url")
    private String viewUrl;

    @Column(length = 300)
    @JsonProperty("form_url")
    private String formUrl;

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

    private String contentEmail;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap attach;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("report_at")
    private LocalDateTime reportAt;

    @JsonProperty("reply_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime replyAt;

    @JsonProperty("send_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime sendAt;

    @JsonProperty("modify_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifyAt;

    @JsonProperty("note_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime noteAt;

    @JsonProperty("closed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime closedAt;

    @JsonProperty("delete_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deleteAt;

    @JsonProperty("image_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime imageAt;

    @JsonProperty("cancel_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime cancelAt;

    @JsonProperty("mail_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime mailAt;

    @JsonProperty("app_id")
    @Column(nullable = false)
    private Long appId;

    @JsonProperty("app_name")
    private String appName;

    public void setStage(ClsStage stage) {
        setStageId(stage == null ? null : stage.getId());
        setStageText(stage == null ? null : stage.getName());
    }

    @JsonProperty("ticket_id")
    public Long getId() {
        return super.getId();
    }

    /**
     * Returns the attach
     */
    public ObjectMap getAttach() {
        if(attach == null) attach = new ObjectMap();
        return attach;
    }

    @JsonProperty("is_upfile")
    public boolean isUpFile() {
        return this.imageAt != null;
    }

    @JsonProperty("is_sendmail")
    public boolean isSendMail() {
        return this.mailId != null;
    }

    @JsonProperty("is_closed")
    public boolean isClosed() {
        return this.closedAt != null;
    }

    public void resetNote() {
        setNoteId(null);
        setNoteAt(null);
    }
}