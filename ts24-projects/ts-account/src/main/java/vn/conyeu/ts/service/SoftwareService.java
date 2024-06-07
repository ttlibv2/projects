package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Software;
import vn.conyeu.ts.repository.SoftwareRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class SoftwareService extends LongUIdService<Software, SoftwareRepo> {

    public SoftwareService(SoftwareRepo domainRepo) {
        super(domainRepo);
    }
}