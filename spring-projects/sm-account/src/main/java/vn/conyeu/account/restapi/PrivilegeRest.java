package vn.conyeu.account.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.account.domain.Privilege;
import vn.conyeu.account.service.PrivilegeService;
import vn.conyeu.common.restapi.LongIdRest;

@RestController
@RequestMapping("/db.privileges")
public class PrivilegeRest extends LongIdRest<Privilege, PrivilegeService> {

    public PrivilegeRest(PrivilegeService service) {
        super(service);
    }

}