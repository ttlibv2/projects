package vn.conyeu.common.restapi;

import vn.conyeu.common.domain.StringId;
import vn.conyeu.common.service.StringIdService;

public abstract class StringRest<E extends StringId<E>, S extends StringIdService<E, ?>> extends DomainRest<E, String, S> {

    public StringRest(S service) {
        super(service);
    }
}