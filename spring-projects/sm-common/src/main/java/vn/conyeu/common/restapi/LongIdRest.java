package vn.conyeu.common.restapi;

import vn.conyeu.common.domain.LongId;
import vn.conyeu.common.service.LongIdService;

public abstract class LongIdRest<E extends LongId<E>, S extends LongIdService<E, ?>> extends DomainRest<E, Long, S> {
    public LongIdRest(S service) {
        super(service);
    }
}