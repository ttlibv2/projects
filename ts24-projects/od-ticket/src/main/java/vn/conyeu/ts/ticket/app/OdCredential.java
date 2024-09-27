package vn.conyeu.ts.ticket.app;

import lombok.Getter;
import lombok.Setter;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUser;

@Getter @Setter()
public class OdCredential {
    private String username;
    private String password;
    private String cookieValue;
    private String csrfToken;
    private ClsUser clsUser;
    private ClsApiCfg clsApi;

    /**
     * Set the clsUser
     * @param clsUser the value
     */
    public void setClsUser(ClsUser clsUser) {
        this.clsUser = clsUser;
        this.cookieValue = clsUser.getCookie();
        this.csrfToken = clsUser.getCsrfToken();
    }

}