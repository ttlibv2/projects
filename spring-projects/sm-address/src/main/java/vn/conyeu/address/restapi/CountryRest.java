package vn.conyeu.address.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.address.domain.Country;
import vn.conyeu.address.service.CountryService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping("/country")
public class CountryRest extends LongUIdRest<Country, CountryService> {

    public CountryRest(CountryService service) {
        super(service);
    }

}