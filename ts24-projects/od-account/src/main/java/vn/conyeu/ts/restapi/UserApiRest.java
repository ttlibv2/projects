package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.SimpleErrors;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.helper.Validators;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.SaveUserApiDto;
import vn.conyeu.ts.dtocls.TsErrors;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.repository.UserApiRepo;
import vn.conyeu.ts.service.ApiInfoService;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.UserApiService;

import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsUserApi)
public class UserApiRest extends LongUIdRest<UserApi, UserApiService> {
    private final ApiInfoService aiService;
    private final OdService odService;
    private final UserApiRepo userApiRepo;
    private final SmartValidator validator;

    @Autowired
    public UserApiRest(UserApiService service, ApiInfoService aiService, OdService odService,
                       UserApiRepo userApiRepo, SmartValidator validator) {
        super(service);
        this.aiService = aiService;
        this.odService = odService;
        this.userApiRepo = userApiRepo;
        this.validator = validator;
    }

    /**
     * Returns all user api without user_id
     *
     * @param userId the user id to get
     */
    @GetMapping("find-by-user")
    public List<UserApi> getAllByUser(@PrincipalId Long userId) {
        return service.findAll(userId);
    }

    /**
     * Returns user api without service name
     *
     * @param userId the user id to get
     * @param name   the service name to get
     */
    @GetMapping("find-by-name/{name}")
    public UserApi findByServiceName(@PrincipalId Long userId, @PathVariable String name) {
        return service.findByServiceName(userId, name).orElse(new UserApi());//.orElseThrow(() -> TsErrors.noUserApiServiceName(name));
    }

    /**
     * Returns all user api without user_id + service_uid
     *
     * @param userId     the user id to get
     * @param serviceUid the service uid to get
     */
    @GetMapping("find-by-uid/{serviceUid}")
    public List<UserApi> findAllByServiceUid(@PrincipalId Long userId, @PathVariable String serviceUid) {
        return service.findAllByServiceUid(userId, serviceUid);
    }

    @PostMapping("check-api-login/{serviceName}")
    public UserApi checkApiLogin(@PrincipalId Long userId, @PathVariable String serviceName) {
        odService.load(userId).loadService(serviceName).login();
        return findByServiceName(userId, serviceName);
    }

    @PostMapping("get-menu-links/{serviceName}")
    public ObjectMap getMenuLinks(@PrincipalId Long userId, @PathVariable String serviceName) {
        ObjectMap object = odService.load(userId).loadService(serviceName).loadMenus();
        aiService.updateMenuLink(serviceName, object);
        return object;
    }

    @PostMapping("copy-api/{serviceName}")
    public ApiInfo copyApi(@PrincipalId Long userId, @PathVariable String serviceName) {
        ApiInfo info = aiService.getByServiceName(serviceName);
        if (!Objects.equals(info.getAllowCopy(), true)) {
            throw BaseException.e400("is_system")
                    .detail("service_name", serviceName)
                    .message("API [%s] không được phép sao chép", serviceName);
        }

        info.setServiceName(null);
        info.setBaseUrl(null);
        info.setAllowCopy(true);
        info.setIsSystem(false);
        info.setId(null);

        // save api_info
        aiService.save(info);

        // save user_api
        Optional<UserApi> optional = service.findByServiceName(userId, serviceName);
        UserApi userApi = optional.isPresent() ? optional.get().reset() : new UserApi();
        userApi.setUniqueId(userId, info);
        service.save(userApi);


        info.setUserApi(userApi);
        return info;
    }

    @PostMapping("save-user/{apiId}")
    public UserApi saveUserApi(@PrincipalId Long userId, @PathVariable Long apiId, @RequestBody ObjectMap info) {
        ApiInfo apiInfo = aiService.getById(apiId);
        return service.save(userId, apiInfo, info);
    }

    @PostMapping("save-info")
    public ApiInfo saveUserInfo(@PrincipalId Long userId, @RequestBody SaveUserApiDto dto) {
        ObjectMap apiDto = dto.getApi_info();
        ObjectMap userDto = dto.getUser_api();
        Long apiId = dto.getApi_id();

        // create new
        if (apiId == null) {

            if (apiDto == null) {
                throw BaseException.e400("miss").detail("data", dto)
                        .message("Dữ liệu truyền thiếu thông tin [api_info]");
            }

            if (userDto == null) {
                throw BaseException.e400("miss").detail("data", dto)
                        .message("Dữ liệu truyền thiếu thông tin [user_api]");
            }

            // check error
            Validators.throwValidate(validator, apiDto);
            Validators.throwValidate(validator, userDto);

            // create ApiInfo
            ApiInfo apiInfo = apiDto.asObject(ApiInfo.class);
            ApiInfo infoNew = aiService.createNew(apiInfo, false);

            // create UserApi
            UserApi userApi = userDto.asObject(UserApi.class);
            userApi.setPassword(userDto.getString("password"));
            userApi.setUniqueId(userId, infoNew);
            service.createNew(userApi, false);

            infoNew.setUserApi(userApi);
            return infoNew;

        }

        // update
        else  {
            ApiInfo apiInfo = null;

            if(apiDto != null) {
                apiInfo = aiService.update(apiId, apiDto)
                        .orElseThrow(() -> aiService.noId(apiId));
            }

            if(userDto != null) {
                apiInfo = apiInfo != null ? apiInfo : aiService.getById(apiId);
                UserApi userApi = service.save(userId, apiInfo, userDto);
                apiInfo.setUserApi(userApi);
            }

            return apiInfo;
        }

    }


//    @PostMapping("save-info/{serviceName}")
//    public UserApi saveInfo(@PrincipalId Long userId, @PathVariable String serviceName, @RequestBody @Valid SaveUserApiDto info) {
//        Optional<UserApi> userApiOptional = service.findByServiceName(userId, serviceName);
//        UserApi userApi;
//
//        if(userApiOptional.isPresent()) {
//            userApi = userApiOptional.get();
//            info.update(userApi);
//        }
//        else {
//            ApiInfo apiInfo = aiService.getByServiceName(serviceName);
//            userApi = info.asUser(true);
//            userApi.setUniqueId(userId, apiInfo);
//        }
//
//        return service.save(userApi);
//    }
}