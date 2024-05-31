package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.ts.domain.Software;
import vn.conyeu.ts.service.SoftwareService;
import vn.conyeu.common.restapi.LongIdRest;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/ts.software")
public class SoftwareRest extends LongIdRest<Software, SoftwareService> {

    public SoftwareRest(SoftwareService service) {
        super(service);
    }

}