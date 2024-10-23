package vn.conyeu.ts.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Software;
import vn.conyeu.ts.repository.SoftwareRepo;
import vn.conyeu.common.service.LongUIdService;

import java.util.List;

@Service
public class SoftwareService extends LongUIdService<Software, SoftwareRepo> {

    public SoftwareService(SoftwareRepo domainRepo) {
        super(domainRepo);
    }

    @Cacheable(cacheNames = "softwares")
    public List<Software> findAll() {
        return super.findAll();
    }

    @Cacheable(cacheNames = "softwares", key = "#pageable")
    public Page<Software> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }
}