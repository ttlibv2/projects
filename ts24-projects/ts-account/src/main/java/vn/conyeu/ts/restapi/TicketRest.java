package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.common.restapi.LongIdRest;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.dtocls.ClsCreateTicketDto;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.TicketService;
import vn.conyeu.ts.ticket.service.OdTicketService;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/ts.ticket")
public class TicketRest extends LongIdRest<Ticket, TicketService> {

    public TicketRest(TicketService service) {
        super(service);
    }

    private OdTicketService odTicketSrv() {
        Long userId = IdentityHelper.extractUserId();
        return OdService.forUser(userId).ticketService();
    }

    @PostMapping("send")
    public Object createTicket(@RequestBody @Valid ClsCreateTicketDto dto) {
        throw new UnsupportedOperationException();
    }





}