package vn.conyeu.ts.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.identity.dtocls.SignUpDto;
import vn.conyeu.ts.domain.TsUser;

import java.io.Serializable;

@Getter
@Setter
public class TsSignUpDto extends SignUpDto {

    @NotBlank
    @JsonProperty("room_code")
    private String roomCode;

    @NotBlank
    @JsonProperty("user_code")
    private String userCode;

    public TsUser createUser() {
        TsUser user = new TsUser();
        user.setRoomCode(roomCode);
        user.setUserCode(userCode);
        return user;
    }
}