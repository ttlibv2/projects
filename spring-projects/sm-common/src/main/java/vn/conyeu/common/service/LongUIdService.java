package vn.conyeu.common.service;

import vn.conyeu.common.domain.LongUId;
import vn.conyeu.common.repository.LongUIdRepo;

public abstract class LongUIdService<S extends LongUId<S>, R extends LongUIdRepo<S>> extends DomainService<S, Long, R> {

    public LongUIdService(R domainRepo) {
        super(domainRepo);
    }
}