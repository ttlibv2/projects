package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.domain.UserConfig;
import vn.conyeu.ts.repository.UserConfigRepo;
import vn.conyeu.common.service.LongIdService;

import java.util.List;
import java.util.Map;

@Service
public class UserConfigService extends LongIdService<UserConfig, UserConfigRepo> {

    public UserConfigService(UserConfigRepo domainRepo) {
        super(domainRepo);
    }

    public ObjectMap loadByUser(Long userId) {
        List<UserConfig> configList = entityRepo.loadByUserId(userId);
        ObjectMap objectMap = ObjectMap.create();
        for(UserConfig config : configList) {
            objectMap.set(config.getCode(), config.getValue());
        }
        return objectMap;
    }
}