package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "questionId"))
@JsonIgnoreProperties({"user"})
//@formatter:on
public class Question extends LongUId<Question> {

    @Column(length = 300, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String reply;

    @JsonProperty("soft_type")
    @Column(length = 50, nullable = false)
    private String softType;

    @ColumnDefault("0")
    private Boolean shared;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private TsUser user;

    @JsonProperty("user_id")
    public Long getUserId() {
        return user == null ? null : user.getId();
    }
}