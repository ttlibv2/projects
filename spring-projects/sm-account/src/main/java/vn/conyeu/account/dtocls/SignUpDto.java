package vn.conyeu.account.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.account.domain.Gender;
import vn.conyeu.account.domain.SignupType;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.commons.utils.Objects;

import java.io.Serializable;
import java.util.Base64;

@Getter @Setter
public class SignUpDto implements Serializable {

    @JsonProperty("signup_type")
    private SignupType signupType = SignupType.email;

    private String email;
    private String phone;

    @NotBlank(message = "secret.empty")
    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private Gender gender;

    public String getSecret() {
        try {
            return Objects.decodeBase64ToString(password);
        }catch (IllegalArgumentException ex) {
            throw new BadRequest("pwd.base64").message("Password invalid");
        }
    }
}