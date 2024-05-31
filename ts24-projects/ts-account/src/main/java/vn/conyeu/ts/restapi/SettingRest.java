package vn.conyeu.ts.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.ts.domain.Setting;
import vn.conyeu.ts.service.SettingService;
import vn.conyeu.common.restapi.LongIdRest;

@RestController
@RequestMapping("/ts.setting")
public class SettingRest extends LongIdRest<Setting, SettingService> {

    public SettingRest(SettingService service) {
        super(service);
    }

}