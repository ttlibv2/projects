package vn.conyeu.ts.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.repository.ApiInfoRepo;
import vn.conyeu.ts.repository.UserApiRepo;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ApiInfoService extends LongUIdService<ApiInfo, ApiInfoRepo> {
    private final UserApiRepo  userApiRepo;

    public ApiInfoService(ApiInfoRepo apiRepo, UserApiRepo userApiRepo) {
        super(apiRepo);
        this.userApiRepo = userApiRepo;
    }

    @Cacheable(cacheNames = "apiInfo", key = "#apiCode")
    public Optional<ApiInfo> findByCode(String apiCode) {
        return repo().findByCode(apiCode);
    }

    public Optional<ApiInfo> findByBaseUrl(String baseUrl) {
        return repo().findByBaseUrl(baseUrl);
    }

    public boolean existsByCode(String apiCode) {
        return repo().existsByCode(apiCode);
    }

    /**
     * If apiCode exist then not save
     * @param apiCode the api code
     * @param consumer the consumer apply info
     * */
    public void tryCreateApi(String apiCode, Consumer<ApiInfo> consumer) {
        if(!existsByCode(apiCode)){
            ApiInfo info = new ApiInfo();
            consumer.accept(info);
            createNew(info);
        }
    }

    public BaseException noCode(String apiCode) {
        return new NotFound().code("api_code.404").detail("api_code", apiCode)
                .message("Không tồn tại dữ liệu '%s'", apiCode);
    }

    public ApiInfo saveUser(Long userId, String apiCode, ObjectMap userApi) {
        userApi.removeIf("password", UserApiService.EMPTY_PWD::equals);

        ApiInfo apiInfo = findByCode(apiCode).orElseThrow(() -> noCode(apiCode));
        Optional<UserApi> userApiOptional = userApiRepo.loadByApiCode(userId, apiCode);

        UserApi user;

        if(userApiOptional.isEmpty()) {
            user = userApi.asObject(UserApi.class);
            user.setUniqueId(userId, apiInfo);
        }
        else {
            user = userApiOptional.get();

            boolean hasReset = false;

            String username = userApi.getString("username");
            if(username != null && !username.equals(user.getUserName())) {
                hasReset = true;
            }

            if(!hasReset) {
                String password = userApi.getString("password");
                if (password != null && !password.equals(user.getPassword())) {
                    hasReset = true;
                }
            }








            user.assignFromMap(userApi);
        }

        apiInfo.setUserApi(userApiRepo.save(user));
        return apiInfo;

    }
}