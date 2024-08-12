package vn.conyeu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUIdDate;

import java.util.List;

//@formatter:off
@Entity @Table(name="eb_book")
@Getter() @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"author"})
@AttributeOverride(name = "id", column = @Column(name = "bookId"))
//@formatter:on
public class Book extends LongUIdDate<Book> {
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId", nullable = false)
    private User author;







    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "seoId")
    private SeoAlias seo;

    public SeoAlias createSeo() {
        SeoAlias alias = new SeoAlias("book");
        setSeo(alias);
        return alias;
    }
}