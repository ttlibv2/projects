package vn.conyeu.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongIdDate;

//@formatter:off
@Entity
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "action", discriminatorType = DiscriminatorType.STRING)
@AttributeOverride(name = "id", column = @Column(name = "baId"))
@JsonIgnoreProperties({"book", "user"})
//@formatter:on
public abstract class BookAction extends LongIdDate<BookAction> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookId", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

//    @Column(length = 100, updatable = false)
//    private String action;

    @Column(length = 100)
    private String value;

}