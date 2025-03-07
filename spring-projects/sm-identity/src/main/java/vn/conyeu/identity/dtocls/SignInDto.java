package vn.conyeu.identity.dtocls;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import vn.conyeu.commons.utils.Jsons;

import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;

public class SignInDto implements Serializable {
    private String username;
    private String password;

    @JsonProperty("sign_type")
    private String signinType = "email";

    public static SignInDto readRequest(HttpServletRequest request) throws IOException {
        return Jsons.readValue(request.getInputStream(), SignInDto.class);
    }

    /**
     * Returns the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the username
     */
    public String getUser() {
        return signinType + "::" + username;
    }

    /**
     * Set the username
     *
     * @param username the value
     */
    public SignInDto setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Returns the password
     */
    public String getPassword() {
        return password;
    }

    public String getDecodePassword() {
        return new String(Base64.getDecoder().decode(password));
    }


    /**
     * Set the password
     *
     * @param password the value
     */
    public SignInDto setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Returns the signinType
     */
    public String getSigninType() {
        return signinType;
    }

    /**
     * Set the signinType
     *
     * @param signinType the value
     */
    public SignInDto setSigninType(String signinType) {
        this.signinType = signinType;
        return this;
    }

    public SignInDto validate() {
        return this;
    }
}