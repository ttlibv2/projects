package vn.conyeu.address.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.address.domain.Address;
import vn.conyeu.address.service.AddressService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping("/address")
public class AddressRest extends LongUIdRest<Address, AddressService> {

    public AddressRest(AddressService service) {
        super(service);
    }

}