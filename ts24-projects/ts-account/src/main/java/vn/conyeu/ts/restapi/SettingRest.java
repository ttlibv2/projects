package vn.conyeu.ts.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.ts.domain.Setting;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.SettingService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping(TsVar.Rest.tsSetting)
public class SettingRest extends LongUIdRest<Setting, SettingService> {

    public SettingRest(SettingService service) {
        super(service);
    }

}