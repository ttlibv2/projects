package vn.conyeu.identity.domain;


import lombok.Getter;

import java.io.Serializable;

@Getter
public class AuthToken implements Serializable {
    private String tokenType;
    private String accessToken;
    private Long expiresIn;

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
}