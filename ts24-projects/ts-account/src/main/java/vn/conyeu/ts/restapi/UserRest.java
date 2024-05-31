package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.restapi.LongIdRest;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.TsUser;
import vn.conyeu.ts.service.UserService;

@RestController
@RequestMapping("/ts.user")
@PreAuthorize("isAuthenticated()")
public class UserRest extends LongIdRest<TsUser, UserService> {

    public UserRest(UserService service) {
        super(service);
    }

    @GetMapping("profile")
    public TsUser getProfile(@PrincipalId Long userId) {
        return service.findById(userId).orElseThrow(() -> service.noId(userId));
    }

}