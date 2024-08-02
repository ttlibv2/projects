package vn.conyeu.ts.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

import java.io.Serializable;


@Getter @Setter
public class SendTicketDto implements Serializable {

    public enum ExistState {
        NONE, CREATE, DELETE, UPDATE
    }

    @NotNull(message = "ticket.uid.notNull")
    @JsonProperty("ticket_id")
    private Long ticketId;

    @NotBlank(message = "ticket.action.notNull")
    private String action;

    // nếu ticket đã tồn tại -> NONE, tao moi, cap nhat, xoa + tao moi
    @JsonProperty("ticket_state")
    private ExistState ticketState = ExistState.NONE;

    // nếu note đã tồn tại -> cập nhật
    @JsonProperty("note_state")
    private ExistState noteState =  ExistState.NONE;

    // key: imageName.jpg, value: imageBase64
    @JsonProperty("images")
    private ObjectMap imageBase64;

    /**
     * Returns the imageBase64
     */
    public ObjectMap getImageBase64() {
        if(Objects.notEmpty(imageBase64)) {
            for(String imageName: imageBase64.keySet()) {
                String newName = prepareImageName(imageName, ".png");
                imageBase64.put(newName, imageBase64.get(imageName));
            }
        }

        return imageBase64;
    }

    public static String prepareImageName(String name, String dot) {
         String dotNew = name.contains(".")?"":dot;
         return name.toLowerCase().trim() + dotNew;
    }
}