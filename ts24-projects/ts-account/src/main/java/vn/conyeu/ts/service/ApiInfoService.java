package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongIdService;
import vn.conyeu.commons.utils.Asserts;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.repository.ApiInfoRepo;
import vn.conyeu.ts.repository.UserApiRepo;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ApiInfoService extends LongIdService<ApiInfo, ApiInfoRepo> {
    private final UserApiRepo userApiRepo;

    public ApiInfoService(ApiInfoRepo apiConfigRepo, UserApiRepo userApiRepo) {
        super(apiConfigRepo);
        this.userApiRepo = userApiRepo;
    }

    public Optional<ApiInfo> findByCode(String apiCode) {
        return entityRepo.findByCode(apiCode);
    }

    public Optional<ApiInfo> findByBaseUrl(String baseUrl) {
        return entityRepo.findByBaseUrl(baseUrl);
    }

    public Optional<UserApi> loadByUser(Long userId, String apiCode) {
        Optional<UserApi> optional = userApiRepo.findByCode(userId, apiCode);
        if(optional.isPresent()) return optional;

        Optional<ApiInfo> optional2 = entityRepo.findByCode(apiCode);
        return optional2.map(config -> createUserApi(userId, config));
    }

    public UserApi loadByUser(Long userId, String apiCode, Consumer<ApiInfo> consumer) {
        Optional<UserApi> optional = loadByUser(userId, apiCode);
        if(optional.isPresent()) return optional.get();
        else
        {
            Asserts.notNull(consumer, "@Consumer<ApiInfo>");
            ApiInfo config = new ApiInfo();
            consumer.accept(config);

            entityRepo.save(config);
            return createUserApi(userId, config);
        }
    }


    private UserApi createUserApi(Long userId, ApiInfo config) {
        UserApi userApi = new UserApi();
        userApi.setApiId(userId, config);
        userApiRepo.save(userApi);
        return userApi;
    }
}