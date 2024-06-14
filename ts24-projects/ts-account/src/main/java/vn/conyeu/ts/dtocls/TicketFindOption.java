package vn.conyeu.ts.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
public class TicketFindOption implements Serializable {

    @JsonProperty("created_min")
    private LocalDateTime createdMin;

    @JsonProperty("created_max")
    private LocalDateTime createdMax;

    @JsonProperty("updated_min")
    private LocalDateTime updatedMin;

    @JsonProperty("updated_max")
    private LocalDateTime updatedMax;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("is_template")
    private Boolean isTemplate;

    @JsonProperty("is_send")
    private Boolean isSend;

    @JsonProperty("is_note")
    private Boolean isNote;

    @JsonProperty("is_attach")
    private Boolean isAttach;

    @JsonProperty("is_close")
    private Boolean isClose;

    @JsonProperty("is_report")
    private Boolean isReport;
}