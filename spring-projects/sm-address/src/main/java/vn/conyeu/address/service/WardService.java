package vn.conyeu.address.service;

import org.springframework.stereotype.Service;
import vn.conyeu.address.domain.Ward;
import vn.conyeu.address.repository.WardRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class WardService extends LongIdService<Ward, WardRepo> {

    public WardService(WardRepo domainRepo) {
        super(domainRepo);
    }
}