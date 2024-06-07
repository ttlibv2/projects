package vn.conyeu.ts.ticket_rest;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.dtocls.Errors;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.odcore.domain.ClsPage;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.ticket.domain.ClsPartner;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.odPartner)
public class OdPartnerRest extends OdBaseRest {

    public OdPartnerRest(OdService odService, UserApiService apiService) {
        super(odService, apiService);
    }

    @GetMapping("search")
    public List<ClsPartner> searchPartner(@RequestParam Map<String, Object> mapQuery, Pageable pg) {
        ObjectMap data = ObjectMap.clone(mapQuery).delete("size");
        ClsPage clsPage = new ClsPage().withLimit(pg.getPageSize());
        return service().partner().search(data, clsPage);
    }

    @GetMapping("get-byid/{partnerId}")
    public ClsPartner getPartnerById( @PathVariable Long partnerId) {
        return service().partner().findById(partnerId).orElseThrow(() -> Errors.notPartnerId(partnerId));
    }

    @PostMapping("create")
    public ClsPartner createPartner(@RequestParam Long companyId, @RequestBody ClsPartner cls) {
        return service().partner().create(companyId, cls);
    }


}