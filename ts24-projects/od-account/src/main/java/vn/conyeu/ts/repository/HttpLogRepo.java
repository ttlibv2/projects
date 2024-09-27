package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.common.repository.LongUIdRepo;
import vn.conyeu.ts.domain.HttpLog;

import java.util.List;
import java.util.Optional;

public interface HttpLogRepo extends LongUIdRepo<HttpLog> {
    Optional<HttpLog> findByRequestId(String requestId);
    boolean existsByRequestId(String requestId);

    @Query("select e from #{#entityName} e where e.model=?1 and e.modelId=?2")
    List<HttpLog> findByModel(String modelName, String modelId);
}