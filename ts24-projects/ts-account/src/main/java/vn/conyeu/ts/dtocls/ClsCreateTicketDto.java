package vn.conyeu.ts.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class ClsCreateTicketDto implements Serializable {

    @NotNull
    @JsonProperty("ticket_id")
    private Long ticketId;

    @NotNull
    private String action;


}