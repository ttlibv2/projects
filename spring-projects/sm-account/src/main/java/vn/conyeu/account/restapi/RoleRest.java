package vn.conyeu.account.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.account.domain.Role;
import vn.conyeu.account.service.RoleService;
import vn.conyeu.common.restapi.LongIdRest;

@RestController
@RequestMapping("/db.roles")
public class RoleRest extends LongIdRest<Role, RoleService> {

    public RoleRest(RoleService service) {
        super(service);
    }

}