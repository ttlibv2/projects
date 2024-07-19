package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.converter.MapString;
import vn.conyeu.common.domain.LongUIdDate;
import vn.conyeu.commons.beans.ObjectMap;

//@formatter:off
@Entity
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Table(indexes = @Index(name = "MODEL_UID", columnList = "model,modelId"))
@AttributeOverride(name = "id", column = @Column(name = "logId"))
//@formatter:on
public class HttpLog extends LongUIdDate<HttpLog> {

    public HttpLog(String requestId) {
        this.requestId = requestId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private TsUser user;

    @ColumnDefault("0")
    @Column(length = 3, nullable = false)
    private Integer status;

    @Column(length = 10, nullable = false, unique = true)
    private String requestId;

    @JsonProperty("model_id")
    private String modelId;

    @JsonProperty("model")
    private String model;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap request;

    @Convert(converter = MapString.class)
    @Column(columnDefinition = "json")
    private ObjectMap response;


}