package vn.conyeu.ts.restapi.odrest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.dtocls.TsErrors;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.service.UserService;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.odUser)
public class OdUserRest extends OdBaseRest {
    private final UserService userService;

    public OdUserRest(OdService odService, UserApiService apiService, UserService userService) {
        super(odService, apiService);
        this.userService = userService;
    }

    @PostMapping("login")
    public ClsUser login(@PrincipalId Long userLogin) {
        ClsUser clsUser = tsApp(userLogin).login();
        userService.update(userLogin, clsUser);
        return clsUser;
    }

    @GetMapping("get-byid/{userId}")
    public ClsUser findUserById( @PathVariable Long userId) {
        return tsApp().user().findById(userId).orElseThrow(() -> TsErrors.noOdUserId(userId));
    }

    @GetMapping(value = "search", params = "keyword")
    public List<ClsUser> findByKeyword(@RequestParam String keyword) {
        return tsApp().user().search(keyword);
    }

    @GetMapping(value = "search", params = "ids")
    public List<ClsUser> findByUserIds(@RequestParam String ids) {
        if(ids == null) return new LinkedList<>();
        List<Long> list = Stream.of(ids.split(",")).map(Long::parseLong).toList();
        return tsApp().user().search(list);
    }


}