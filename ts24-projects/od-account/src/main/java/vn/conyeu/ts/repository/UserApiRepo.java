package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.common.repository.LongUIdRepo;
import vn.conyeu.ts.domain.UserApi;

import java.util.List;
import java.util.Optional;

public interface UserApiRepo extends LongUIdRepo<UserApi> {

    @Query("select e from #{#entityName} e where e.user.id=?1 and lower(e.api.appName)=lower(?2) ")
    Optional<UserApi> findByAppName(Long userId, String name);

    @Query("select e from #{#entityName} e where e.user.id=?1 and e.api.id=?2")
    Optional<UserApi> findByApiId(Long userId, Long apiId);

    @Query("select e from #{#entityName} e where e.user.id=?1")
    List<UserApi> findAll(Long userId);

    @Query("select e from #{#entityName} e where e.user.id=?1 and e.api.appUID=?2")
    List<UserApi> findAllByAppUID(Long userId, String uid);

    @Query("select count(e) > 0 from #{#entityName} e where e.api.id=?1 and e.user.id=?2")
    boolean existsByApiId(Long apiId, Long userId);
}