package vn.conyeu.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class LongUIdDate<E extends LongUIdDate<E>> extends LongUId<E> {

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //@Convert(converter = DateTimeToString.class)
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    //@Convert(converter = DateTimeToString.class)
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected LocalDateTime updatedAt;

    public LongUIdDate() {
    }

    public LongUIdDate(Long entityId) {
        super(entityId);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LongUIdDate<E> createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LongUIdDate<E> updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}