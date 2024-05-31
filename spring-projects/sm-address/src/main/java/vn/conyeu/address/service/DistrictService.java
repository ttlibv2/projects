package vn.conyeu.address.service;

import org.springframework.stereotype.Service;
import vn.conyeu.address.domain.District;
import vn.conyeu.address.repository.DistrictRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class DistrictService extends LongIdService<District, DistrictRepo> {

    public DistrictService(DistrictRepo domainRepo) {
        super(domainRepo);
    }
}