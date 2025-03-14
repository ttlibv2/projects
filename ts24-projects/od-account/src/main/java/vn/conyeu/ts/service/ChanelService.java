package vn.conyeu.ts.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Cacheable(cacheNames = "chanels", key = "#pageable")
    public Page<Chanel> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }

    @Cacheable(cacheNames = "findAllById", key = "#p0")
    public List<Chanel> findAllById(Iterable<Long> longs) {
        return super.findAllById(longs);
    }
}