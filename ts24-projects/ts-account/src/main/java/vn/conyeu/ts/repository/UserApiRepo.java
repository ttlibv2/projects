package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.common.repository.LongUIdRepo;
import vn.conyeu.ts.domain.UserApi;
import vn.conyeu.ts.odcore.domain.ClsUser;

import java.util.List;
import java.util.Optional;

public interface UserApiRepo extends LongUIdRepo<UserApi> {

    @Query("select e from #{#entityName} e where e.user.id=?1 and e.api.code=?2")
    Optional<UserApi> loadByApiCode(Long userId, String apiCode);

    @Query("select e from #{#entityName} e where e.user.id=?1 and e.api.id=?2")
    Optional<UserApi> loadByApiId(Long userId, Long apiId);

    @Query("select e from #{#entityName} e where e.user.id=?1 and e.api.baseUrl=?2")
    Optional<UserApi> findByApiUrl(Long userId, String baseUrl);

    @Query("select e from #{#entityName} e where e.user.id=?1")
    List<UserApi> getAllByUser(Long userId);

}