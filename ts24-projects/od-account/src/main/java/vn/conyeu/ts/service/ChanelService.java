package vn.conyeu.ts.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Chanel;
import vn.conyeu.ts.repository.ChanelRepo;
import vn.conyeu.common.service.LongUIdService;

import java.util.List;

@Service
public class ChanelService extends LongUIdService<Chanel, ChanelRepo> {

    public ChanelService(ChanelRepo domainRepo) {
        super(domainRepo);
    }

    @Cacheable(cacheNames = "chanels")
    public List<Chanel> findAll() {
        return super.findAll();
    }

    @Cacheable(cacheNames = "findAllById", key = "#p0")
    public List<Chanel> findAllById(Iterable<Long> longs) {
        return super.findAllById(longs);
    }
}