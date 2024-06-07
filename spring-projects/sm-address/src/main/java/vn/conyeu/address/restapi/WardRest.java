package vn.conyeu.address.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.address.domain.Ward;
import vn.conyeu.address.service.WardService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping("/ward")
public class WardRest extends LongUIdRest<Ward, WardService> {

    public WardRest(WardService service) {
        super(service);
    }

}