package vn.conyeu.book.service;

import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.Rate;
import vn.conyeu.book.repository.RateRepo;
import vn.conyeu.common.service.LongIdDateService;

@Service
public class RateService extends LongIdDateService<Rate, RateRepo> {

    public RateService(RateRepo domainRepo) {
        super(domainRepo);
    }
}