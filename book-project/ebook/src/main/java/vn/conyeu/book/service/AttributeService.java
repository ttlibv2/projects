package vn.conyeu.book.service;

import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.Attribute;
import vn.conyeu.book.repository.AttributeRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class AttributeService extends LongIdService<Attribute, AttributeRepo> {

    public AttributeService(AttributeRepo domainRepo) {
        super(domainRepo);
    }
}