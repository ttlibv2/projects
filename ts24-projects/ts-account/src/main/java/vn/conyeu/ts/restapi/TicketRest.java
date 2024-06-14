package vn.conyeu.ts.restapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
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

    @Override
    public Page<Ticket> getAll(ObjectMap params, Pageable pageable) {
        return super.getAll(params, pageable);
    }

}