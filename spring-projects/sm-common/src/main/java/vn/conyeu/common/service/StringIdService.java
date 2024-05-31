package vn.conyeu.common.service;

import vn.conyeu.common.domain.StringId;
import vn.conyeu.common.repository.StringIdRepo;

public class StringIdService<S extends StringId<S>, R extends StringIdRepo<S>> extends DomainService<S, String, R> {

    public StringIdService(R domainRepo) {
        super(domainRepo);
    }
}