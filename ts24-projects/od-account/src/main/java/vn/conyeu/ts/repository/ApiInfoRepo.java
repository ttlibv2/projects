package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.ts.domain.ApiInfo;
import vn.conyeu.common.repository.LongUIdRepo;

import java.util.List;
import java.util.Optional;

public interface ApiInfoRepo extends LongUIdRepo<ApiInfo> {
    Optional<ApiInfo> findByServiceName(String name);
    List<ApiInfo> findByServiceUid(String uid);
    boolean existsByServiceName(String name);
    boolean existsByServiceUid(String uid);
    boolean existsByServiceUidAndBaseUrl(String suid, String baseUrl);

    @Query("select count(e) > 0 from #{#entityName} e where e.serviceUid=?1 and e.serviceName=?2")
    boolean existsBySUID(String suid, String sname);
}