package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.common.restapi.LongIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.identity.domain.Account;
import vn.conyeu.identity.service.AccountService;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.TsUser;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.TsUserDto;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.ApiInfoService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.service.UserService;
import vn.conyeu.ts.ticket.service.TSApp;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(TsVar.Rest.tsUser)
@PreAuthorize("isAuthenticated()")
public class UserRest extends LongIdRest<TsUser, UserService> {
    final AccountService accountService;
    final UserApiService userApiService;
    final ApiInfoService apiInfoService;

    public UserRest(UserService service, AccountService accountService, UserApiService userApiService, ApiInfoService apiInfoService) {
        super(service);
        this.accountService = accountService;
        this.userApiService = userApiService;
        this.apiInfoService = apiInfoService;
    }

    @PostMapping("connect")
    public TsUser connectApp(@PrincipalId Long userId, @RequestBody @Valid TsUserDto dto) {
        if (service.existsById(userId)) return service.getById(userId);
        else {
            TsUser user = dto.createUser();
            user.setId(userId);
            user = service.createNew(user);

            // create api info
            String apiCode = TSApp.APP_UID;
            Optional<ApiInfo> optional = apiInfoService.findByAppName(apiCode);
            if (optional.isEmpty()) throw new NotFound("no_api").detail("api_code", apiCode)
                    .message("Khong tim thay cau hinh api [%s]", apiCode);

            UserApi userApi = new UserApi();
            userApi.setUser(user);
            userApi.setUserName(dto.getTsUser());
            userApi.setPassword(dto.getTsSecret());
            userApi.setAutoLogin(dto.getTsAutoLogin());
            userApi.setApi(optional.get());

            userApiService.createNew(userApi);

            return user;
        }
    }

    @GetMapping("get-profile")
    public TsUser getProfile(@PrincipalId Long userId) {
        if (!service.existsById(userId)) {
            Account account = accountService.getById(userId);
            String fullName = account.getInfo().getFullName();
            TsUser user = new TsUser();
            user.setId(account.getId());
            user.setTsName(fullName);
            user.setUserCode("setUserCode");
            user.setRoomCode("setRoomCode");
            user.setReqUpdate(true);
            return service.createNew(user);
        }
        return service.findById(userId).orElseThrow(() -> service.noId(userId));
    }

    @GetMapping("get-config")
    public TsUser getUserConfig(@PrincipalId Long userId) {
        TsUser tsUser = getProfile(userId);

        Long tsAppId = tsUser.getTsApp();
        if(tsAppId != null) {
            ObjectMap links = apiInfoService.getById(tsAppId).getLinks();
            tsUser.setTsLinks(links);
        }

        return tsUser;
    }

    @PostMapping("update-profile")
    public TsUser updateProfile(@PrincipalId Long userId, @RequestBody Map<String, Object> bodyMap) {
        Optional<TsUser> optional = service.update(userId, ObjectMap.fromMap(bodyMap));
        return optional.orElseThrow(() -> service.noId(userId));
    }
}