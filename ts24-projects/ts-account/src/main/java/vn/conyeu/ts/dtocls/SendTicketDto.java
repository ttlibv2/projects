package vn.conyeu.ts.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.ticket_rest.TicketAction;
import java.io.Serializable;


@Getter @Setter
public class SendTicketDto implements Serializable {

    @NotBlank(message = "ticket.uid.notNull")
    @JsonProperty("ticket_id")
    private Long ticketId;

    @NotBlank(message = "ticket.action.notNull")
    private String action;

    // nếu ticket đã tồn tại -> cập nhật
    @JsonProperty("update_ticket")
    private boolean updateTicket = false;

    // nếu note đã tồn tại -> cập nhật
    @JsonProperty("update_note")
    private boolean updateNote = false;

    // key: imageName.jpg, value: imageBase64
    @JsonProperty("image_base64")
    private ObjectMap imageBase64;



    /**
     * Returns the imageBase64
     */
    public ObjectMap getImageBase64() {
        if(Objects.notEmpty(imageBase64)) {
            for(String imageName: imageBase64.keySet()) {
                String dot = imageName.contains(".") ? "" : ".png";
                String newName = (imageName + dot).toLowerCase();
                imageBase64.put(newName, imageBase64.get(imageName));
            }
        }

        return imageBase64;
    }
}