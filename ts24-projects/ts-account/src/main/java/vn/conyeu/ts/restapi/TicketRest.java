package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.ChanelService;
import vn.conyeu.ts.service.TicketService;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsTicket)
public class TicketRest extends LongUIdRest<Ticket, TicketService> {
    final ChanelService chanelService;

    public TicketRest(TicketService service, ChanelService chanelService) {
        super(service);
        this.chanelService = chanelService;
    }

    @Override
    public Page<Ticket> getAll(Map<String, Object> params, Pageable pageable) {
        params.put("user_id", IdentityHelper.extractUserId());
        Page<Ticket> ticketPage = super.getAll(params, pageable);
        for(Ticket ticket:ticketPage) {

            // chanel
            List<Long> chanelIds = ticket.getChanelIds();
            if(Objects.notEmpty(chanelIds)) ticket.setChanels(chanelService.findAllById(chanelIds));

        }

        return ticketPage;
    }

    @Override
    public Page<Ticket> getAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("save")
    public Ticket saveTicket(@RequestBody @Valid ObjectMap object, @PrincipalId Long userId) {


        return service.saveTicket(userId, object);




//        object.setUser(new TsUser(userId));
//
//        if(Objects.notEmpty(object.getChanels())) {
//            object.setChanelIds(object.getChanels().stream().map(LongUId::getId).toList());
//
//            if(Objects.isNull(object.getSupportHelp()) && Objects.isNull(object.getSupportHelpId())) {
//                object.setSupportHelp(object.getChanels().get(0));
//            }
//
//        }
//
//        return super.createObject(object);
    }


}