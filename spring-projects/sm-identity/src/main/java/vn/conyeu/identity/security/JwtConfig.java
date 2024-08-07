package vn.conyeu.identity.security;

import io.jsonwebtoken.io.Decoders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

//@Configuration
@Component
@ConfigurationProperties("security.jwt")
public class JwtConfig {

    private String secret = "33743677397A24432646294A404D635166546A576E5A7234753778214125442A";
    private Long expiration = 3600000L;
    private String algorithm = "HmacSHA256";
    private String publicKeyLocation;
    private String tokenType = "Bearer";
    private String headerAuth = "Authorization";
    private String authUri = "/auth/signin";

    /**
     * Returns the algorithm
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Set the algorithm
     *
     * @param algorithm the value
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Returns the secretKey
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Set the secretKey
     *
     * @param secret the value
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Returns the expiration
     */
    public Long getExpiration() {
        return expiration;
    }

    /**
     * Set the expiration
     *
     * @param expiration the value
     */
    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    /**
     * Returns the publicKeyLocation
     */
    public String getPublicKeyLocation() {
        return publicKeyLocation;
    }

    /**
     * Set the publicKeyLocation
     *
     * @param publicKeyLocation the value
     */
    public void setPublicKeyLocation(String publicKeyLocation) {
        this.publicKeyLocation = publicKeyLocation;
    }

    /**
     * Returns the tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Set the tokenType
     *
     * @param tokenType the value
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Returns the headerAuth
     */
    public String getHeaderAuth() {
        return headerAuth;
    }

    /**
     * Set the headerAuth
     *
     * @param headerAuth the value
     */
    public void setHeaderAuth(String headerAuth) {
        this.headerAuth = headerAuth;
    }

    /**
     * Returns the authUri
     */
    public String getAuthUri() {
        return authUri;
    }

    /**
     * Set the authUri
     *
     * @param authUri the value
     */
    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }


}