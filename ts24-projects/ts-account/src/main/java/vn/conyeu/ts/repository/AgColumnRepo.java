package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.ts.domain.AgColumn;
import vn.conyeu.common.repository.LongIdRepo;

import java.util.List;

public interface AgColumnRepo extends LongIdRepo<AgColumn> {

    @Query("select e from #{#entityName} e where e.table.code=?1")
    List<AgColumn> findByTableCode(String agCode);

    List<AgColumn> findByTableId(Long tableId);
}