package vn.conyeu.ts.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.ts.domain.AgTable;
import vn.conyeu.ts.repository.AgTableRepo;

import java.util.List;
import java.util.Optional;

@Service
public class AgTableService extends LongUIdService<AgTable, AgTableRepo> {

    public AgTableService(AgTableRepo domainRepo) {
        super(domainRepo);
    }

    @Cacheable(cacheNames = "ag_table", key = "{#methodName, #tableCode}")
    public Optional<AgTable> findByCode(String tableCode) {
        return repo().findByCode(tableCode);
    }

    public List<AgTable> findByParentId(Long parentId) {
        return repo().findByParentId(parentId);
    }

    @Override
    public AgTable saveAndReturn(AgTable entity) {
        clearCache();
        return super.saveAndReturn(entity);
    }

    @CacheEvict(cacheNames = "ag_table")
    public void clearCache() {
    }
}