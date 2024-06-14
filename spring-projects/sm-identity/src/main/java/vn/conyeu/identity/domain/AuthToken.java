package vn.conyeu.identity.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class AuthToken implements Serializable {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("user_id")
    private Long userId;

    /**
     * Set the tokenType
     *
     * @param tokenType the value
     */
    public AuthToken setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    /**
     * Set the accessToken
     *
     * @param accessToken the value
     */
    public AuthToken setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /**
     * Set the expiresIn
     *
     * @param expiresIn the value
     */
    public AuthToken setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    /**
     * Set the userId
     *
     * @param userId the value
     */
    public AuthToken setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}