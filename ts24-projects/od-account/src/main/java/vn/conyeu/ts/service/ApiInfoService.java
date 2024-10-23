package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.helper.Validators;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.dtocls.TsErrors;
import vn.conyeu.ts.repository.ApiInfoRepo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ApiInfoService extends LongUIdService<ApiInfo, ApiInfoRepo> {
    private final SmartValidator validator;

    public ApiInfoService(ApiInfoRepo apiRepo, SmartValidator validator) {
        super(apiRepo);
        this.validator = validator;
    }

    private void e400(String field, String value) {
        if(Objects.isBlank(field)) throw BaseException.e400("%s").detail(field, value)
                .message("Thông tin [%s] không được rỗng", field);
    }

    private void checkSNameExist(String sname) {
        if(existsByAppName(sname)) {
            throw BaseException.e400("app_name")
                    .detail("app_name", sname)
                    .message("Thông tin [app_name] tồn tại.");
        }
    }

    private void checkBaseUrlExist(String suid, String baseUrl) {
        if (existsByBaseUrl(suid, baseUrl)) {
            throw BaseException.e400("base_url")
                    .detail("app_uid", suid).detail("base_url", baseUrl)
                    .message("Thông tin [base_url] tồn tại.");
        }
    }

    public ApiInfo copy(Long copyId, ApiInfo info) {
        ApiInfo source = getById(copyId);
        boolean allowCopy = Objects.equals(source.getAllowCopy(), true);

        if(!allowCopy) {
            throw BaseException.e400("allow_copy")
                    .detail("copy_id", copyId).detail("app_name", source.getAppName())
                    .message("Ứng dụng [%s] không được phép thực hiện sao chép", source.getTitle());
        }

        info.setId(null);
        info.setTargetId(copyId);
        return createNew(info, true);
    }


    @Override
    public ApiInfo createNew(ApiInfo info) {
        return createNew(info, true);
    }

    public ApiInfo createNew(ApiInfo info, boolean hasValidate) {
        if(hasValidate) {
            Validators.throwValidate(validator, info);
        }

        checkSNameExist(info.getAppName());
        checkBaseUrlExist(info.getAppUID(), info.getBaseUrl());
        return saveAndReturn(info);
    }

    @Override
    public Optional<ApiInfo> update(Long apiId, ApiInfo info) {
        Optional<ApiInfo> optional = findById(apiId);
        if(optional.isEmpty()) return Optional.empty();

        ApiInfo infoOld = optional.get();
        infoOld.assignFromEntity(info);

        Validators.throwValidate(validator, infoOld);
        return Optional.of(saveAndReturn(infoOld));
    }

    @Override
    public Optional<ApiInfo> update(Long apiId, ObjectMap overrides) {
        Optional<ApiInfo> optional = findById(apiId);
        if(optional.isEmpty()) return Optional.empty();

        ApiInfo infoOld = optional.get();
        infoOld.assignFromMap(overrides);

        Validators.throwValidate(validator, infoOld);
        return Optional.of(saveAndReturn(infoOld));
    }


    public Optional<ApiInfo> findByUIDName(String uid, String name) {
        return repo().findByUIDName(uid, name);
    }

    /**
     * Find api without app_name
     * @param name the app name to find
     * */
    public ApiInfo getByAppName(String name) {
        return findByAppName(name).orElseThrow(() -> TsErrors.noAppName(name));
    }

    /**
     * Find api without app_name
     * @param name the app name to find
     * */
    public Optional<ApiInfo> findByAppName(String name) {
        return repo().findByAppName(name);
    }

    /**
     * Find api without app_uid
     * @param uid the app uid to find
     * */
    public List<ApiInfo> findByAppUID(String uid) {
        return repo().findByAppUID(uid);
    }

    /**
     * Returns true if app_name exist
     * @return {boolean}
     * */
    public boolean existsByAppName(String name) {
        return repo().existsByAppName(name);
    }

    /**
     * Returns true if app_uid exist
     * @return {boolean}
     * */
    public boolean existsByAppUID(String uid) {
        return repo().existsByAppUID(uid);
    }

    /**
     * Returns true if [app_uid + base_url] exist
     * @return {boolean}
     * */
    public boolean existsByBaseUrl(String appUID, String baseUrl) {
        return repo().existsByAppUIDAndBaseUrl(appUID, baseUrl);
    }


    /**
     * If app_name exist then not save
     * @param appName the app name
     * @param consumer the consumer apply info
     * */
    public void tryCreateApi(String appName, Consumer<ApiInfo> consumer) {
        if(!existsByAppName(appName)){
            ApiInfo info = new ApiInfo();
            consumer.accept(info);
            createNew(info);
        }
    }

    public void updateMenuLink(String appName, ObjectMap links) {
        ApiInfo apiInfo = getByAppName(appName);
        apiInfo.setLinks(links);
        saveAndReturn(apiInfo);
    }

}