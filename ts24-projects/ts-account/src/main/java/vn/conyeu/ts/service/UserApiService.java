package vn.conyeu.ts.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.dtocls.Errors;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.repository.SoftwareRepo;
import vn.conyeu.ts.repository.UserApiRepo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class UserApiService extends LongUIdService<UserApi, UserApiRepo> {
    static final String EMPTY_PWD = "*".repeat(8);

    public UserApiService(UserApiRepo apiRepo) {
        super(apiRepo);
    }

    //@Cacheable(value = "UserApi", key = "{#userId, #apiCode}")
    public Optional<UserApi> findByApiCode(Long userId, String apiCode) {
        return repo().loadByApiCode(userId, apiCode);
    }

    public Optional<UserApi> findByApiId(Long userId, Long apiId) {
        return repo().loadByApiId(userId, apiId);
    }

    public Optional<UserApi> findByApiUrl(Long userId, String baseUrl) {
        return repo().findByApiUrl(userId, baseUrl);
    }

    public List<UserApi> getAllByUser(Long userId) {
        return repo().getAllByUser(userId);
    }

    @CacheEvict(value = "UserApi", key = "{#userId, #apiCode}")
    public UserApi updateByCode(Long userId, String apiCode, Consumer<UserApi> consumerApi) {
        Optional<UserApi> optional = findByApiCode(userId, apiCode);
        if(optional.isEmpty()) throw Errors.noUserApiCode(apiCode);
        else {
            UserApi userApi = optional.get();
            consumerApi.accept(userApi);
            userApi.getUserInfo().setTsUser(null);
            return save(userApi);
        }
    }

    @Override
    public UserApi save(UserApi entity) {

        if(Objects.equals(entity.getPassword(), EMPTY_PWD)) {
            entity.setPassword(null);
        }

        return super.save(entity);
    }
}