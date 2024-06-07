package vn.conyeu.common.restapi;

import vn.conyeu.common.domain.LongUId;
import vn.conyeu.common.service.LongUIdService;

public abstract class LongUIdRest<E extends LongUId<E>, S extends LongUIdService<E, ?>> extends DomainRest<E, Long, S> {
    public LongUIdRest(S service) {
        super(service);
    }
}