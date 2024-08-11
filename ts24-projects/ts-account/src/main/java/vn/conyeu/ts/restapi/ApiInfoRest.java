package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.Errors;
import vn.conyeu.ts.dtocls.SaveUserApiDto;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.ApiInfoService;
import vn.conyeu.ts.service.UserApiService;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsApiInfo)
public class ApiInfoRest extends LongUIdRest<ApiInfo, ApiInfoService> {
    private final UserApiService userApiService;

    public ApiInfoRest(ApiInfoService service, UserApiService userApiRepo) {
        super(service);
        this.userApiService = userApiRepo;
    }

    @GetMapping(path = "get-by-code/{code}")
    public ApiInfo getUserByCode(@PrincipalId Long userId, @RequestParam Map<String, ?> params, @PathVariable String code) {
        ObjectMap queryMap = ObjectMap.fromMap(params);
        Long qUserId = queryMap.getLong("userid", null);

        ApiInfo apiInfo = service.findByCode(code).orElseThrow(() -> service.noCode(code));

        if(qUserId != null) {
            if(!Objects.equals(userId, qUserId)) {
                throw new BadRequest().code("user.invalid")
                        .message("userid not support for api");
            }
            else {
                apiInfo.setUserApi(userApiService.findByApiCode(userId, code).orElse(null));
            }
        }

        return apiInfo;
    }

    @PostMapping("save-user/{code}")
    public UserApi saveInfo(@PrincipalId Long userId, @PathVariable String code, @RequestBody @Valid SaveUserApiDto info) {
        ApiInfo apiInfo = service.findByCode(code).orElseThrow(() -> service.noCode(code));
        Optional<UserApi> userApiOptional = userApiService.findByApiCode(userId, code);

        UserApi user;
        if(userApiOptional.isEmpty()) {
            user = info.asUser(true);
            user.setUniqueId(userId, apiInfo);
        }

        else {
            user = info.update(userApiOptional.get());
        }

        return userApiService.save(user);
    }



}