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
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.service.ApiInfoService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.service.UserService;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
            String apiCode = OdTicketService.SERVICE_NAME;
            Optional<ApiInfo> optional = apiInfoService.findByServiceName(apiCode);
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
            user.setFullName(fullName);
            user.setUserCode("setUserCode");
            user.setRoomCode("setRoomCode");
            user.setRequiredUpdate(true);
            return service.createNew(user);
        }
        return service.findById(userId).orElseThrow(() -> service.noId(userId));
    }

    @GetMapping("get-config")
    public ObjectMap getUserConfig(@PrincipalId Long userId) {
        ObjectMap map = ObjectMap.fromJson(getProfile(userId));
        List<UserApi> users = userApiService.findAll(userId);
        Map<String, ClsUser> clsUserMap = users.stream().collect(Collectors.toMap(
                UserApi::getServiceUid,
                UserApi::getClsUserBasic
        ));

        map.set("user_api", clsUserMap);
        return map;
    }


}