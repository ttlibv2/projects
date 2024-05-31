package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongIdService;
import vn.conyeu.ts.domain.AgTable;
import vn.conyeu.ts.repository.AgTableRepo;

import java.util.Optional;

@Service
public class AgTableService extends LongIdService<AgTable, AgTableRepo> {

    public AgTableService(AgTableRepo domainRepo) {
        super(domainRepo);
    }


    public Optional<AgTable> findByCode(String tableCode) {
        return entityRepo.findByCode(tableCode);
    }
}