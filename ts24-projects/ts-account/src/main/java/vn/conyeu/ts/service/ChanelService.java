package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Chanel;
import vn.conyeu.ts.repository.ChanelRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class ChanelService extends LongIdService<Chanel, ChanelRepo> {

    public ChanelService(ChanelRepo domainRepo) {
        super(domainRepo);
    }
}