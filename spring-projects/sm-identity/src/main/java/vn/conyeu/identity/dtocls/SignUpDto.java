package vn.conyeu.identity.dtocls;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.common.converter.ListStringToString;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.identity.domain.SignupType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class SignUpDto implements Serializable {

    private String email;

    private String phone;

    @NotBlank(message = "secret.empty")
    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String gender;

    private String avatar;

    @Max(100)
    @JsonProperty("cover_img")
    private String coverImg;

    private String bio;

    private LocalDate dob;

    @JsonProperty("nick_name")
    private String nickName;

    private String slogan;

    @Max(50)
    private String identity;

    private String education;

    private List<String> languages;

    @JsonProperty("signup_type")
    private SignupType signupType = SignupType.email;

    public String getDecodePwd() {
        return Objects.decodeBase64ToString(password);
    }


}