package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.Errors;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.ApiInfoService;
import vn.conyeu.ts.service.UserApiService;

import java.util.Optional;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsApiInfo)
public class ApiInfoRest extends LongUIdRest<ApiInfo, ApiInfoService> {
    private final UserApiService userApiRepo;

    public ApiInfoRest(ApiInfoService service, UserApiService userApiRepo) {
        super(service);
        this.userApiRepo = userApiRepo;
    }

    @GetMapping("/user/get-by-code/{apiCode}")
    public UserApi getUserByCode(@PrincipalId Long userId, @PathVariable String apiCode) {
        Optional<UserApi> optional = userApiRepo.findByApiCode(userId, apiCode);
        return optional.orElseGet(UserApi::new);
    }

    @PostMapping("/user/save-info/{apiCode}")
    public UserApi saveUserApi(@PrincipalId Long userId, @PathVariable String apiCode, @RequestBody @Valid UserApi info) {
        ApiInfo api = service.findByCode(apiCode).orElseThrow(() -> Errors.noApi(apiCode));
        return userApiRepo.save(userId, api, info);
    }



}