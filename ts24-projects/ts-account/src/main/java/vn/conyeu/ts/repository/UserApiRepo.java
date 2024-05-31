package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.common.repository.DomainRepo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.domain.UserApi.UserApiId;

import java.util.Optional;

public interface UserApiRepo extends DomainRepo<UserApi, UserApiId> {

    Optional<UserApi> findById(UserApiId userApiId);

    @Query("select e from #{#entityName} e where e.id.user.id=?1 and e.id.api.code=?2")
    Optional<UserApi> findByCode(Long userId, String apiCode);

    @Query("select e from #{#entityName} e where e.id.user.id=?1 and e.id.api.baseUrl=?2")
    Optional<UserApi> findByBaseUrl(Long userId, String baseUrl);
}