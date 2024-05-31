package vn.conyeu.ts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongId;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "questionId"))
//@formatter:on
public class Question extends LongId<Question> {

    @Column(length = 300, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String reply;

    @Column(length = 50, nullable = false)
    private String softType;

    @ColumnDefault("0")
    private Boolean shared;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private TsUser user;


}