package vn.conyeu.address.service;

import org.springframework.stereotype.Service;
import vn.conyeu.address.domain.Country;
import vn.conyeu.address.repository.CountryRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class CountryService extends LongIdService<Country, CountryRepo> {

    public CountryService(CountryRepo domainRepo) {
        super(domainRepo);
    }
}