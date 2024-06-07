package vn.conyeu.ts.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.ts.domain.TsUser;

import java.io.Serializable;

@Getter
@Setter
public class TsSignUpDto implements Serializable {

    @NotBlank
    @JsonProperty("room_code")
    private String roomCode;

    @NotBlank
    @JsonProperty("email")
    private String email;

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("secret")
    private String secret;

    @NotBlank
    @JsonProperty("user_code")
    private String userCode;

    public TsUser createUser() {
        TsUser user = new TsUser();
        user.setRoomCode(roomCode);
        user.setTsEmail(email);
        user.setTsName(name);
        user.setUserCode(userCode);
        return user;
    }
}