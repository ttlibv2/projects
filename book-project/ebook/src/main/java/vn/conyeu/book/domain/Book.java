package vn.conyeu.book.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongIdDate;

import java.util.List;
import java.util.Set;

//@formatter:off
@Entity @Table
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "id"))
//@formatter:on
public class Book extends LongIdDate<Book> {
    private String poster;
    private String title;
    private String source;

    @ColumnDefault("0")
    private Integer coin;

    @ColumnDefault("0")
    private Integer countWork;

    @ColumnDefault("0")
    private Integer totalChapter;

    @ColumnDefault("0")
    private Boolean isComic; // truyện tranh

    @Column(length = 500)
    private String summary;

    @ColumnDefault("0")
    @Enumerated(EnumType.ORDINAL)
    private BookState state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId")
    private User author;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "seoId")
    private SeoAlias seo;

    @JoinTable(name = "bookCate",
            joinColumns = @JoinColumn(name = "cateId"),
            inverseJoinColumns = @JoinColumn(name = "bookId"))
    private Set<Category> categories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL)
    private List<Chapter> chapters;

}