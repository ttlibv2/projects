package vn.conyeu.ts.odcore.domain;

import java.io.Serializable;

public class ClsCredential implements Serializable {
    private String cookie;
    private String csrftoken;
    private String username;
    private String password;
}