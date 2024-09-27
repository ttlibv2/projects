package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.SystemLog;
import vn.conyeu.ts.repository.SystemLogRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class SystemLogService extends LongUIdService<SystemLog, SystemLogRepo> {

    public SystemLogService(SystemLogRepo domainRepo) {
        super(domainRepo);
    }
}