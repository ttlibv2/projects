package vn.conyeu.ts.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.ts.domain.TsUser;

import java.io.Serializable;
import java.util.List;

@Getter @Setter
public class TsUserDto implements Serializable {

    @NotBlank @JsonProperty("email")
    private String email;

    @NotBlank @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("user_code")
    private String userCode;

    @NotBlank  @JsonProperty("room_code")
    private String roomCode;

    @JsonProperty("required_update")
    private Boolean requiredUpdate;

    @NotBlank
    @JsonProperty("ts_user")
    private String tsUser;

    @NotBlank
    @JsonProperty("ts_secret")
    private String tsSecret;

    @JsonProperty("ts_auto_login")
    private Boolean tsAutoLogin = true;

    @JsonProperty("try_login")
    private Boolean tryLogin = true;

    @JsonProperty("api_codes")
    private List<String> apiCodes;

    public TsUser createUser() {
        TsUser user = new TsUser();
        user.setTsEmail(email);
        user.setTsName(name);
        user.setRoomCode(roomCode);
        user.setUserCode(userCode);
        return user;
    }
}