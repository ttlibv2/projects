package vn.conyeu.common.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class LongId<E extends LongId<E>> extends DomainId<E, Long> {

    @Id @Column(unique = true, updatable = false)
    protected Long id;

    public LongId() {
    }

    public LongId(Long entityId) {
        this.id = entityId;
    }

    public Long getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id the value
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }
}