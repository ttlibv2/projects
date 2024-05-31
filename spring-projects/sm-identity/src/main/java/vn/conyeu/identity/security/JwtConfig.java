package vn.conyeu.identity.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.jwt")
public class JwtConfig {

    private String secretKey = "33743677397A24432646294A404D635166546A576E5A7234753778214125442A";
    private Long expiration = 1000 * 60 * 24L;
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
    public JwtConfig setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    /**
     * Returns the secretKey
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Set the secretKey
     *
     * @param secretKey the value
     */
    public JwtConfig setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
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
    public JwtConfig setExpiration(Long expiration) {
        this.expiration = expiration;
        return this;
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
    public JwtConfig setPublicKeyLocation(String publicKeyLocation) {
        this.publicKeyLocation = publicKeyLocation;
        return this;
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
    public JwtConfig setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
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
    public JwtConfig setHeaderAuth(String headerAuth) {
        this.headerAuth = headerAuth;
        return this;
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
    public JwtConfig setAuthUri(String authUri) {
        this.authUri = authUri;
        return this;
    }
}