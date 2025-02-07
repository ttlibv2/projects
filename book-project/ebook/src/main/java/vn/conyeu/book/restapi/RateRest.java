package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.Rate;
import vn.conyeu.book.service.RateService;
import vn.conyeu.common.restapi.LongIdDateRest;

@RestController
@RequestMapping("/rate")
public class RateRest extends LongIdDateRest<Rate, RateService> {

    public RateRest(RateService service) {
        super(service);
    }

}