package vn.conyeu.ts.ticket.app;

import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUser;
import java.util.function.BiConsumer;


public class OdApp {
    private final OdCredential credential;
    private final OdHttpClient client;
    private BiConsumer<ClsApiCfg, ClsUser> loginConsumer;

    public OdApp(OdCredential credential, OdHttpClient client) {
        this.credential = credential;
        this.client = client;
    }

    public OdApp login() {
        ClsUser clsUser = null;//user().login();
        credential.setClsUser(clsUser);

        if(loginConsumer != null) {
            ClsApiCfg clsApi = credential.getClsApi();
            loginConsumer.accept(clsApi, clsUser);
        }


        return this;
    }


}