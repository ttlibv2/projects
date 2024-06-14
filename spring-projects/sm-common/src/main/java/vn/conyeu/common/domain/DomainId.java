package vn.conyeu.common.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.MapperHelper;

import java.io.Serializable;

/**
 * @param <E> the entity type
 * @param <Id> the id of entity: Long, String,....
 * */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DomainId<E extends DomainId<E, Id>, Id extends Serializable> {

    /**
     * Returns entity id
     * @return entity id
     * */
    public abstract Id getId();

    /**
     * Set id for entity
     * @param id the entity id
     * */
    public abstract void setId(Id id);

    /**
     * Copy properties from entity other
     * @param map the other entity
     * */
    public void assignFromObject(Object map) {
        throw new UnsupportedOperationException();
    }

    public void assignFromMap(ObjectMap map) {
        MapperHelper.update(this, map);
    }

    /**
     * Copy properties from entity other
     * @param other the other entity
     * @see #assignFromEntity(DomainId, String...)
     * */
    public final void assignFromEntity(E other) {
        assignFromEntity(other, "id");
    }

    /**
     * Copy properties from entity other
     * @param other the other entity
     * */
    public void assignFromEntity(E other, String...ignoreProperties) {
        BeanUtils.copyProperties(other, this, ignoreProperties);
    }


}