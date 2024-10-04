package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.common.repository.LongUIdRepo;

import java.util.List;
import java.util.Optional;

public interface ApiInfoRepo extends LongUIdRepo<ApiInfo> {
    Optional<ApiInfo> findByAppName(String name);
    List<ApiInfo> findByAppUID(String uid);
    boolean existsByAppName(String name);
    boolean existsByAppUID(String uid);
    boolean existsByAppUIDAndBaseUrl(String uid, String baseUrl);

    @Query("select count(e) > 0 from #{#entityName} e where e.appUID=?1 and e.appName=?2")
    boolean existsByAppUID(String uid, String sname);

    @Query("select count(e) from #{#entityName} e where e.id=?1 or e.targetId=?1")
    Long countTargetBy(Long apiId);

    @Query("select e from #{#entityName} e where e.appUID=?1 and e.appName=?2")
    Optional<ApiInfo> findByUIDName(String uid, String name);
}