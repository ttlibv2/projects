package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.SaveUserApiDto;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.ApiInfoService;
import vn.conyeu.ts.service.Ts24Service;
import vn.conyeu.ts.service.UserApiService;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsUserApi)
public class UserApiRest extends LongUIdRest<UserApi, UserApiService> {
    private final ApiInfoService aiService;
    private final Ts24Service odService;

    @Autowired
    public UserApiRest(UserApiService service, ApiInfoService aiService, Ts24Service odService) {
        super(service);
        this.aiService = aiService;
        this.odService = odService;
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
     * Returns user api without app name
     *
     * @param userId the user id to get
     * @param name   the app name to get
     */
    @GetMapping("find-by-name/{name}")
    public UserApi findByAppName(@PrincipalId Long userId, @PathVariable String name) {
        return service.findByAppName(userId, name).orElse(new UserApi());
    }

    /**
     * Returns all user api without [user_id + app_uid]
     *
     * @param userId     the user id to get
     * @param appUID the app uid to get
     */
    @GetMapping("find-by-uid/{appUID}")
    public List<UserApi> findAllByAppUID(@PrincipalId Long userId, @PathVariable String appUID) {
        return service.findAllByAppUID(userId, appUID);
    }

    @PostMapping("check-api-login/{appName}")
    public UserApi checkApiLogin(@PrincipalId Long userId, @PathVariable String appName) {
        odService.forUser(userId).loadApp(appName).login();
        return findByAppName(userId, appName);
    }

    @PostMapping("get-menu-links/{appName}")
    public ObjectMap getMenuLinks(@PrincipalId Long userId, @PathVariable String appName) {
        ObjectMap object = odService.forUser(userId).loadApp(appName).loadMenus();
        aiService.updateMenuLink(appName, object);
        return object;
    }

    @PostMapping("save-user/{apiId}")
    public UserApi saveUserApi(@PrincipalId Long userId, @PathVariable Long apiId, @RequestBody ObjectMap info) {
        ApiInfo apiInfo = aiService.getById(apiId);
        return service.save(userId, apiInfo, info);
    }

    @PostMapping("save-info")
    public ApiInfo saveUserInfo(@PrincipalId Long userId, @Valid @RequestBody SaveUserApiDto dto) {
        if(dto.hasCopyApi()) {
            ApiInfo infoNew = dto.asInfo(true);
            UserApi userNew = dto.asCredential(true);

            // create api_info
            ApiInfo info = aiService.copy(dto.getSource_id(), infoNew);

            // create user_api
            userNew.setUniqueId(userId, info);
            service.createNew(userNew, false);

            return info.userApi(userNew);
        }

        //dto.hasEdit()
        else {
            ApiInfo infoNew = dto.asInfo(false);
            ObjectMap userNew = dto.getUser_api();
            Long apiId = dto.getSource_id();

            if(infoNew == null && userNew == null) {
                throw BaseException.e400("miss").message("api_info = null or user_api = null");
            }

            if(infoNew != null) {
                infoNew = aiService.update(apiId, infoNew)
                        .orElseThrow(() -> aiService.noId(apiId));
            }

            if(userNew != null) {
                infoNew = infoNew != null ? infoNew : aiService.getById(apiId);
                UserApi userApi = service.save(userId, infoNew, userNew);
                infoNew.setUserApi(userApi);
            }

            // update api_config
            String appName = infoNew.getAppName();
            odService.forUser(userId).loadConfig(appName, true);

            return infoNew;
        }

    }

}