package vn.conyeu.address.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.address.domain.District;
import vn.conyeu.address.service.DistrictService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping("/district")
public class DistrictRest extends LongUIdRest<District, DistrictService> {

    public DistrictRest(DistrictService service) {
        super(service);
    }

}