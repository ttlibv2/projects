package vn.conyeu.ts.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.ApiInfoService;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsApiInfo)
public class ApiInfoRest extends LongUIdRest<ApiInfo, ApiInfoService> {

    @Autowired
    public ApiInfoRest(ApiInfoService service) {
        super(service);
    }

    @GetMapping("search")
    public Page<ApiInfo> getAll(@RequestParam Map<String, Object> query, Pageable pageable) {
        ObjectMap mapQuery = ObjectMap.fromMap(query);

        // service_uid
        if(mapQuery.containsKey("suid")) {
            List<ApiInfo> list = findByServiceUid(mapQuery.getString("suid"));
            return new PageImpl<>(list, pageable, list.size());
        }

        // service_name
        if(mapQuery.containsKey("sname")) {
            ApiInfo apiInfo = findByServiceName(mapQuery.getString("sname"));
            return new PageImpl<>(List.of(apiInfo), pageable, 1);
        }

        return super.getAll(pageable);
    }

    @GetMapping("check-service-name/{sname}")
    public ObjectMap existsByServiceName(@PathVariable String sname) {
        boolean exist = service.existsByServiceName(sname);
        return ObjectMap.setNew("service_name", sname).set("exist", exist);
    }

    @GetMapping("check-base-url/{suid}/{baseUrl}")
    public ObjectMap existsByServiceName(@PathVariable String suid, @PathVariable String baseUrl) {
        boolean exist = service.existsByBaseUrl(suid, baseUrl);
        return ObjectMap.setNew("service_uid", suid)
                .set("base_url", baseUrl).set("exist", exist);
    }

    @GetMapping("find-by-uid/{uid}")
    public List<ApiInfo> findByServiceUid(@PathVariable String uid) {
        return service.findByServiceUid(uid);
    }

    @GetMapping("find-by-name/{name}")
    public ApiInfo findByServiceName(@PathVariable String name) {
        return service.getByServiceName(name);
    }

    @GetMapping("get-by-id/{apiId}")
    public ApiInfo getById(@PathVariable Long apiId) {
        return super.getById(apiId);
    }
}