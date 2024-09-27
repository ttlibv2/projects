package vn.conyeu.ts.restapi.odrest;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.ts.dtocls.TsErrors;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.odcore.domain.ClsSearch;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.ticket.domain.ClsPartner;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.odPartner)
public class OdPartnerRest extends OdBaseRest {

    public OdPartnerRest(OdService odService, UserApiService apiService) {
        super(odService, apiService);
    }

    @PostMapping("search")
    public Page<ClsPartner> searchPartner(@RequestBody ClsSearch clsSearch) {
        return service().partner().search(clsSearch);
    }

    @GetMapping("get-byid/{partnerId}")
    public ClsPartner getPartnerById( @PathVariable Long partnerId) {
        return service().partner().findById(partnerId).orElseThrow(() -> TsErrors.notPartnerId(partnerId));
    }

    @PostMapping("create")
    public ClsPartner createPartner(@RequestBody ClsPartner cls) {
        return service().partner().create( cls);
    }


}