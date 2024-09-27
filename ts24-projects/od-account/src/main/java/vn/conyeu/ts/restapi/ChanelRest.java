package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.ts.domain.Chanel;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.ChanelService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsChanel)
public class ChanelRest extends LongUIdRest<Chanel, ChanelService> {

    public ChanelRest(ChanelService service) {
        super(service);
    }

    @PostMapping("create")
    public Chanel createObject(@RequestBody Chanel object) {
        return super.createObject(object);
    }
}