package vn.conyeu.ts.restapi.odrest;

import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.ticket.service.OdTicketService;

public abstract class OdBaseRest {

    protected final OdService odService;
    protected final UserApiService apiService;

    public OdBaseRest(OdService odService, UserApiService apiService) {
        this.odService = odService;
        this.apiService = apiService;
    }

    protected final OdTicketService service() {
        return odService.load()
                .updateConfig().ticketService();
    }
}