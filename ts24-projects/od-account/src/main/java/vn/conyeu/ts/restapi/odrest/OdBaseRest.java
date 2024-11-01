package vn.conyeu.ts.restapi.odrest;

import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.ts.service.Ts24Service;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.ticket.service.TSApp;

public abstract class OdBaseRest {

    protected final Ts24Service odService;
    protected final UserApiService apiService;

    public OdBaseRest(Ts24Service odService, UserApiService apiService) {
        this.odService = odService;
        this.apiService = apiService;
    }

    protected final TSApp tsApp(Long userId) {
        return odService.forUser(userId).loadApp(TSApp.APP_UID);
    }

    protected final TSApp tsApp() {
        return tsApp(IdentityHelper.extractUserId());
    }
}