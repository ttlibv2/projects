package vn.conyeu.ts.ticket_rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.dtocls.Errors;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.UserApiService;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.odUser)
public class OdUserRest extends OdBaseRest {

    public OdUserRest(OdService odService, UserApiService apiService) {
        super(odService, apiService);
    }

    @GetMapping("login")
    public Object login(@PrincipalId Long userLogin) {
        ClsUser clsUser = service().login();
        return ObjectMap.setNew("result", clsUser)
                .set("message", "Đăng nhập thành công");
    }

    @GetMapping("get-byid/{userId}")
    public ClsUser findUserById( @PathVariable Long userId) {
        return service().user().findById(userId).orElseThrow(() -> Errors.noOdUserId(userId));
    }

    @GetMapping(value = "search", params = "keyword")
    public List<ClsUser> findByKeyword(@RequestParam String keyword) {
        return service().user().search(keyword);
    }

    @GetMapping(value = "search", params = "keyword")
    public List<ClsUser> findByUserIds(@RequestParam String ids) {
        if(ids == null) return new LinkedList<>();
        List<Integer> list = Stream.of(ids.split(",")).map(Integer::parseInt).toList();
        return service().user().search(list);
    }

//    @GetMapping("load_menus")
//    public ObjectMap loadMenus() {
//        return service().webClient().loadMenuForUser();
//    }

}