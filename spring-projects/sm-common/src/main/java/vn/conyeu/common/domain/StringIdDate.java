package vn.conyeu.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public abstract class StringIdDate<E extends StringIdDate<E>> extends StringId<E> {

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

    public StringIdDate() {
    }

    public StringIdDate(String entityId) {
        super(entityId);
    }
}