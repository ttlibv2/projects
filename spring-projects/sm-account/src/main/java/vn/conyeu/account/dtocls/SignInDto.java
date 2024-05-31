package vn.conyeu.account.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.account.domain.SignupType;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.commons.utils.Objects;

import java.io.Serializable;
import java.util.Base64;

@Getter @Setter
public class SignInDto implements Serializable {

    @JsonProperty("sign_type")
    private SignupType type;

    @JsonProperty("username")
    @NotBlank(message = "user.empty")
    private String userName;

    @JsonProperty("password")
    @NotBlank(message = "secret.empty")
    private String password;

    @JsonProperty("google_code")
    private String googleCode;

    public String getUserWithPrefix() {
        return type.name().toLowerCase() + "::" + userName;
    }

    public String getSecret() {
        try {
            return Objects.decodeBase64ToString(password);
        }catch (IllegalArgumentException ex) {
            throw new BadRequest("pwd.base64").message(ex.getMessage());
        }

    }
}