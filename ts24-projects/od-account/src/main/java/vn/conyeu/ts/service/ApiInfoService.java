package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
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
        if(Objects.isBlank(field)) throw BaseException.e400("%s")
                .message("Thông tin [%s] không được rỗng", field, field);
    }

    private void checkSNameExist(String sname) {
        if(existsByServiceName(sname)) {
            throw BaseException.e400("service_name")
                    .message("Thông tin [service_name] tồn tại.");
        }
    }

    private void checkBaseUrlExist(String suid, String baseUrl) {
        if(existsByBaseUrl(suid, baseUrl)) {
            throw BaseException.e400("base_url")
                    .message("Thông tin [base_url] tồn tại.");
        }
    }

//    private void checkAllowCopy(String suid) {
//        if(repo().existsBySUID(suid, suid)) {
//            throw BaseException.e400("service_uid")
//                    .message("service_uid [%s] không được tạo mới dòng tiếp theo");
//        }
//    }

    @Override
    public ApiInfo createNew(ApiInfo info) {
        return createNew(info, true);
    }

    public ApiInfo createNew(ApiInfo info, boolean hasValidate) {
        if(hasValidate) {
            Validators.throwValidate(validator, info);
        }

        checkSNameExist(info.getServiceName());
        checkBaseUrlExist(info.getServiceUid(), info.getBaseUrl());
//        checkAllowCopy(info.getServiceUid());

        return save(info);
    }

    @Override
    public Optional<ApiInfo> update(Long apiId, ApiInfo info) {
        Optional<ApiInfo> optional = findById(apiId);
        if(optional.isEmpty()) return Optional.empty();

        ApiInfo infoOld = optional.get();
        infoOld.assignFromEntity(info);

        Validators.throwValidate(validator, infoOld);
        return Optional.of(save(infoOld));
    }

    @Override
    public Optional<ApiInfo> update(Long apiId, ObjectMap overrides) {
        Optional<ApiInfo> optional = findById(apiId);
        if(optional.isEmpty()) return Optional.empty();

        ApiInfo infoOld = optional.get();
        infoOld.assignFromMap(overrides);

        Validators.throwValidate(validator, infoOld);
        return Optional.of(save(infoOld));
    }

    /**
     * Find api without service_name
     * @param name the service name to find
     * */
    public ApiInfo getByServiceName(String name) {
        return findByServiceName(name).orElseThrow(() -> TsErrors.noServiceName(name));
    }

    /**
     * Find api without service_name
     * @param name the service name to find
     * */
    public Optional<ApiInfo> findByServiceName(String name) {
        return repo().findByServiceName(name);
    }

    /**
     * Find api without service_uid
     * @param uid the service uid to find
     * */
    public List<ApiInfo> findByServiceUid(String uid) {
        return repo().findByServiceUid(uid);
    }

    /**
     * Returns true if service_name exist
     * @return {boolean}
     * */
    public boolean existsByServiceName(String name) {
        return repo().existsByServiceName(name);
    }

    /**
     * Returns true if service_uid exist
     * @return {boolean}
     * */
    public boolean existsByServiceUid(String uid) {
        return repo().existsByServiceUid(uid);
    }

    /**
     * Returns true if [service_uid + base_url] exist
     * @return {boolean}
     * */
    public boolean existsByBaseUrl(String serviceUid, String baseUrl) {
        return repo().existsByServiceUidAndBaseUrl(serviceUid, baseUrl);
    }


    /**
     * If apiCode exist then not save
     * @param serviceName the api code
     * @param consumer the consumer apply info
     * */
    public void tryCreateApi(String serviceName, Consumer<ApiInfo> consumer) {
        if(!existsByServiceName(serviceName)){
            ApiInfo info = new ApiInfo();
            consumer.accept(info);
            createNew(info);
        }
    }

    public final BaseException noServiceName(String name) {
        return TsErrors.noServiceName(name);
    }

    public void updateMenuLink(String serviceName, ObjectMap links) {
        ApiInfo apiInfo = getByServiceName(serviceName);
        apiInfo.setLinks(links);
        save(apiInfo);
    }
}