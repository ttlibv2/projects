package vn.conyeu.common.service;

import vn.conyeu.common.domain.LongId;
import vn.conyeu.common.repository.LongIdRepo;

public abstract class LongIdService<S extends LongId<S>, R extends LongIdRepo<S>> extends DomainService<S, Long, R> {

    public LongIdService(R domainRepo) {
        super(domainRepo);
    }
}