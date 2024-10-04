package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.SaveUserApiDto;
import vn.conyeu.ts.dtocls.TsErrors;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.ApiInfoService;
import vn.conyeu.ts.service.UserApiService;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsApiInfo)
public class ApiInfoRest extends LongUIdRest<ApiInfo, ApiInfoService> {
    private final UserApiService uaService;

    @Autowired
    public ApiInfoRest(ApiInfoService service, UserApiService uaService) {
        super(service);
        this.uaService = uaService;
    }

    @GetMapping("get-in-one")
    public Page<ApiInfo> getAll(@PrincipalId Long userId,@RequestParam Map<String, Object> query, Pageable pageable) {
        ObjectMap mapQuery = ObjectMap.fromMap(query);
        String uid = mapQuery.getString("uid");
        String name = mapQuery.getString("name");
        Long apiId = mapQuery.getLong("api_id");

        if(apiId != null) {
            ApiInfo apiInfo = service.getById(apiId);
            return new PageImpl<>(List.of(apiInfo), pageable, 1);
        }

        else if(Objects.allNotNull(uid, name)) {
            ApiInfo apiInfo = service.findByUIDName(uid, name).orElseThrow(() -> TsErrors.noAppName(name));
            return new PageImpl<>(List.of(apiInfo), pageable, 1);
        }

        // app_uid
        else if(Objects.notBlank(uid)) {
            List<ApiInfo> list = findByAppUID(userId, uid, false);
            return new PageImpl<>(list, pageable, list.size());
        }

        // app_name
        else if(Objects.notBlank(name)) {
            ApiInfo apiInfo = findByAppName(userId, name, false);
            return new PageImpl<>(List.of(apiInfo), pageable, 1);
        }

        return super.getAll(pageable);
    }

    @GetMapping("check-app-name/{sname}")
    public ObjectMap checkAppName(@PathVariable String sname) {
        boolean exist = service.existsByAppName(sname);
        return ObjectMap.setNew("app_name", sname).set("exist", exist);
    }

    @GetMapping("check-base-url/{suid}/{baseUrl}")
    public ObjectMap checkBaseUrl(@PathVariable String suid, @PathVariable String baseUrl) {
        boolean exist = service.existsByBaseUrl(suid, baseUrl);
        return ObjectMap.setNew("app_uid", suid)
                .set("base_url", baseUrl).set("exist", exist);
    }

    @GetMapping("find-by-uid/{uid}")
    public List<ApiInfo> findByAppUID(@PrincipalId Long userId, @PathVariable String uid, @RequestParam(required = false) Boolean credential) {
        List<ApiInfo> list = service.findByAppUID(uid);
        if(credential != null && credential) {
            list.forEach(ai -> {
                UserApi userApi = uaService.findByApiId(userId, ai.getId()).orElse(null);
                ai.setUserApi(userApi);
            });
        }
        return list;
    }

    @GetMapping("find-by-name/{name}")
    public ApiInfo findByAppName(@PrincipalId Long userId, @PathVariable String name, @RequestParam(required = false) Boolean credential) {
        ApiInfo ai = service.getByAppName(name);
        if(credential != null && credential) {
            UserApi userApi = uaService.findByApiId(userId, ai.getId()).orElse(null);
            ai.setUserApi(userApi);
        }
        return ai;
    }

    @GetMapping("find-by-id/{apiId}")
    public ApiInfo getById(@PathVariable Long apiId) {
        return super.getById(apiId);
    }

    @PostMapping("save-info")
    public ApiInfo saveUserInfo(@Valid @RequestBody SaveUserApiDto dto) {
        ApiInfo infoNew = dto.asInfo(true);Long apiId = dto.getSource_id();
        if(dto.hasCopyApi()) return service.copy(dto.getSource_id(), infoNew);
        else return service.update(apiId, infoNew).orElseThrow(() -> service.noId(apiId));
    }

}