package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Setting;
import vn.conyeu.ts.repository.SystemSettingRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class SettingService extends LongIdService<Setting, SystemSettingRepo> {

    public SettingService(SystemSettingRepo domainRepo) {
        super(domainRepo);
    }
}