package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.repository.UserApiRepo;
import vn.conyeu.ts.service.ApiInfoService;

import java.util.Optional;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsApiInfo)
public class ApiInfoRest extends LongUIdRest<ApiInfo, ApiInfoService> {
    private final UserApiRepo userApiRepo;

    public ApiInfoRest(ApiInfoService service, UserApiRepo userApiRepo) {
        super(service);
        this.userApiRepo = userApiRepo;
    }

    @GetMapping("/user/get-by-code/{apiCode}")
    public UserApi getUserByCode(@PrincipalId Long userId, @PathVariable String apiCode) {
        Optional<UserApi> optional = userApiRepo.loadByApiCode(userId, apiCode);
        return optional.orElseGet(UserApi::new);
    }



}