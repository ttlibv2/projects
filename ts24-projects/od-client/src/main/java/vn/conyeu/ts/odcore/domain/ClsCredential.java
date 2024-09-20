package vn.conyeu.ts.odcore.domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ClsCredential implements Serializable {
    private String cookie;
    private String csrftoken;
    private String username;
    private String password;
    private ClsUserContext context;

    /**
     * Set the cookie
     *
     * @param cookie the value
     */
    public ClsCredential setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    /**
     * Set the csrftoken
     *
     * @param csrftoken the value
     */
    public ClsCredential setCsrftoken(String csrftoken) {
        this.csrftoken = csrftoken;
        return this;
    }

    /**
     * Set the username
     *
     * @param username the value
     */
    public ClsCredential setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Set the password
     *
     * @param password the value
     */
    public ClsCredential setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Set the context
     *
     * @param context the value
     */
    public ClsCredential setContext(ClsUserContext context) {
        this.context = context;
        return this;
    }
}