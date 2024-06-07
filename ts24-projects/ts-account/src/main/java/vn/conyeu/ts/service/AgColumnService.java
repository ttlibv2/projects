package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.AgColumn;
import vn.conyeu.ts.repository.AgColumnRepo;
import vn.conyeu.common.service.LongUIdService;

import java.util.List;

@Service
public class AgColumnService extends LongUIdService<AgColumn, AgColumnRepo> {

    public AgColumnService(AgColumnRepo domainRepo) {
        super(domainRepo);
    }

    /**
     * Get all column without tableCode
     * @param tableCode the code of table
     * */
    public List<AgColumn> findByTableCode(String tableCode) {
        return repo().findByTableCode(tableCode);
    }

    /**
     * Get all column without tableId
     * @param tableId the id of table
     * */
    public List<AgColumn> findByTableId(Long tableId) {
        return repo().findByTableId(tableId);
    }
}