package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.helper.Validators;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.TsErrors;
import vn.conyeu.ts.repository.UserApiRepo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class UserApiService extends LongUIdService<UserApi, UserApiRepo> {
    static final String EMPTY_PWD = "*".repeat(8);
    private final SmartValidator validator;

    public UserApiService(UserApiRepo apiRepo, SmartValidator validator) {
        super(apiRepo);
        this.validator = validator;
    }

    /**
     * Returns user api without service name
     * @param userId the user id to get
     * @param serviceName the service name to get
     * */
    public Optional<UserApi> findByServiceName(Long userId, String serviceName) {
        return repo().findByServiceName(userId, serviceName);
    }

    /**
     * Returns user api without api_id
     * @param userId the user id to get
     * @param apiId the api_id to get
     * */
    public Optional<UserApi> findByApiId(Long userId, Long apiId) {
        return repo().findByApiId(userId, apiId);
    }

    /**
     * Returns all user api without user_id
     * @param userId the user id to get
     * */
    public List<UserApi> findAll(Long userId) {
        return repo().findAll(userId);
    }

    /**
     * Returns all user api without user_id + service_uid
     * @param userId the user id to get
     * @param serviceUid the service uid to get
     * */
    public List<UserApi> findAllByServiceUid(Long userId, String serviceUid) {
        return repo().findAllByServiceUid(userId, serviceUid);
    }

    public boolean existsByApiId(Long apiId, Long userId) {
        return repo().existsByApiId(apiId, userId);
    }

    public UserApi updateConsumer(Long userId, String serviceName, Consumer<UserApi> consumer) {
        Optional<UserApi> optional = findByServiceName(userId, serviceName);
        if(optional.isEmpty()) throw TsErrors.noUserApiServiceName(serviceName);

        UserApi userApi = optional.get();
        consumer.accept(userApi);
        userApi.getUserInfo().setTsUser(null);
        return save(userApi);
    }

    @Override
    public UserApi createNew(UserApi info) {
        return createNew(info, true);
    }

    public UserApi createNew(UserApi info, boolean hasValidate) {
        if(hasValidate) {
            Validators.throwValidate(validator, info);
        }

        Long apiId = info.getApiId();
        Long userId = info.getUserId();
        if(existsByApiId(apiId, userId)) {
            throw BaseException.e400("api_id")
                    .detail("api_id", apiId).detail("user_id", userId)
                    .message("Không thể tạo API [ID=%s] cho User [%s] -- APi này đã tồn tại", apiId, userId);
        }

        return save(info);
    }

    @Override
    public Optional<UserApi> update(Long userApiId, ObjectMap overrides) {
        return super.update(userApiId, overrides);
    }

    public UserApi save(Long userId, ApiInfo apiInfo, ObjectMap info) {
        Optional<UserApi> optional = findByApiId(userId, apiInfo.getId());
        if(optional.isEmpty()) {
            UserApi userApi = info.asObject(UserApi.class);
            userApi.setUniqueId(userId, apiInfo);
            return createNew(userApi);
        }
        else {
            UserApi userApi = optional.get();
            userApi.assignFromMap(info);
            return save(userApi, true);
        }
    }


    public UserApi save(UserApi info) {
        return save(info, false);
    }

    private UserApi save(UserApi info, boolean hasValidate) {

        if(Objects.equals(info.getPassword(), EMPTY_PWD)) {
            info.setPassword(null);
        }

        if(hasValidate) {
            Validators.throwValidate(validator, info);
        }

        return super.save(info);
    }
}