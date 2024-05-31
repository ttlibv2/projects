package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.SystemLog;
import vn.conyeu.ts.repository.SystemLogRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class SystemLogService extends LongIdService<SystemLog, SystemLogRepo> {

    public SystemLogService(SystemLogRepo domainRepo) {
        super(domainRepo);
    }
}