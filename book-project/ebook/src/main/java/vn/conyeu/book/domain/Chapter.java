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
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "chapterId"))
@JsonIgnoreProperties({"book"})
//@formatter:on
public class Chapter extends LongIdDate<Chapter> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "seoId")
    private SeoAlias seo;

    @OrderBy("asc")
    private Long position;

}