package vn.conyeu.common.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class StringId<E extends StringId<E>> extends DomainId<E, String>{

    @Id
    @Column(unique = true, updatable = false, length=36)
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    public StringId() {
    }

    public StringId(String entityId) {
        this.id = entityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}