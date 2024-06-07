package vn.conyeu.address.service;

import org.springframework.stereotype.Service;
import vn.conyeu.address.domain.Province;
import vn.conyeu.address.repository.ProvinceRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class ProvinceService extends LongUIdService<Province, ProvinceRepo> {

    public ProvinceService(ProvinceRepo domainRepo) {
        super(domainRepo);
    }
}