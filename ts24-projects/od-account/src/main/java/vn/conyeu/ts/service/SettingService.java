package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Setting;
import vn.conyeu.ts.repository.SystemSettingRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class SettingService extends LongUIdService<Setting, SystemSettingRepo> {

    public SettingService(SystemSettingRepo domainRepo) {
        super(domainRepo);
    }
}