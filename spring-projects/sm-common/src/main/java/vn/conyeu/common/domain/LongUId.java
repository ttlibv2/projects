package vn.conyeu.common.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass @EntityListeners(AuditingEntityListener.class)
public abstract class LongUId<E extends LongUId<E>> extends DomainId<E, Long>  {

    @Id @Column(unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public LongUId() {
    }

    public LongUId(Long entityId) {
        this.id = entityId;
    }

    /**
     * Returns the id
     */
    @Override
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