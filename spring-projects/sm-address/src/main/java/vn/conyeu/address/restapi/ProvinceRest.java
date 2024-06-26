package vn.conyeu.address.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.address.domain.Province;
import vn.conyeu.address.service.ProvinceService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping("/province")
public class ProvinceRest extends LongUIdRest<Province, ProvinceService> {

    public ProvinceRest(ProvinceService service) {
        super(service);
    }

}