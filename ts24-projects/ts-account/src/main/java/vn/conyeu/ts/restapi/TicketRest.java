package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.domain.LongUId;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.helper.IdentityHelper;
import vn.conyeu.ts.domain.Ticket;
import vn.conyeu.ts.domain.TsUser;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.TicketService;

import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsTicket)
public class TicketRest extends LongUIdRest<Ticket, TicketService> {

    public TicketRest(TicketService service) {
        super(service);
    }

    @Override
    public Page<Ticket> getAll(Map<String, Object> params, Pageable pageable) {
        params.put("user_id", IdentityHelper.extractUserId());
        return super.getAll(params, pageable);
    }

    @Override
    public Page<Ticket> getAll(Pageable pageable) {
        return super.getAll(pageable);
    }

    @PostMapping("create")
    public Ticket createObject(@RequestBody @Valid Ticket object, @PrincipalId Long userId) {
        object.setUser(new TsUser(userId));

        if(Objects.notEmpty(object.getChanels())) {
            object.setChanelIds(object.getChanels().stream().map(LongUId::getId).toList());

            if(Objects.isNull(object.getSupportHelp()) && Objects.isNull(object.getSupportHelpId())) {
                object.setSupportHelp(object.getChanels().get(0));
            }

        }



        return super.createObject(object);
    }
}