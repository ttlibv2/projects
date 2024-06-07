package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.TicketService;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsTicket)
public class TicketRest extends LongUIdRest<Ticket, TicketService> {

    public TicketRest(TicketService service) {
        super(service);
    }

//    private OdTicketService odTicketSrv() {
//        Long userId = IdentityHelper.extractUserId();
//        return OdService.forUser(userId).ticketService();
//    }





}