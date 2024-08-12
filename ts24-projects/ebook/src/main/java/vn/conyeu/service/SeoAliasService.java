package vn.conyeu.service;

import org.springframework.stereotype.Service;
import vn.conyeu.domain.SeoAlias;
import vn.conyeu.repository.SeoAliasRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class SeoAliasService extends LongUIdService<SeoAlias, SeoAliasRepo> {

    public SeoAliasService(SeoAliasRepo domainRepo) {
        super(domainRepo);
    }
}