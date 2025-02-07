package vn.conyeu.book.service;

import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.SeoAlias;
import vn.conyeu.book.repository.SeoAliasRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class SeoAliasService extends LongIdService<SeoAlias, SeoAliasRepo> {

    public SeoAliasService(SeoAliasRepo domainRepo) {
        super(domainRepo);
    }
}