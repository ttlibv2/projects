package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.ts.domain.AgTable;
import vn.conyeu.common.repository.LongUIdRepo;

import java.util.List;
import java.util.Optional;

public interface AgTableRepo extends LongUIdRepo<AgTable> {

    Optional<AgTable> findByCode(String tableCode);

    @Query("select e from #{#entityName} e where e.parent.id=?1")
    List<AgTable> findByParentId(Long parentId);
}