package vn.conyeu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUIdDate;

import java.util.ArrayList;
import java.util.List;

//@formatter:off
@Entity @Table(name="eb_book_dto")
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "bookId"))
//@formatter:on
public class BookDto extends LongUIdDate<BookDto> {
    private String title;
    private String author;

    @Column(length = 500)
    private String categories;

    @Column(length = 2000)
    private String summary;

    @Column(length = 100)
    private String source;

    @Column(length = 100)
    private String state;

    @Column(length = 500)
    private String poster;

    private Double totalRate;

    private Long totalReview;

    private Long totalChapter;

    private Boolean hot;

    private Boolean full;

    @Column(length = 150)
    private String lastChapterUrl;

    @Column(length = 50)
    private String lastChapterId;

    @Column(length = 300)
    private String seoUrl;

    @Column(length = 50)
    private String externalId;

    @Column(length = 50)
    private String service;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "book")
    private List<ChapterDto> chapters;

    /**
     * Returns the chapters
     */
    public List<ChapterDto> getChapters() {
        if(chapters == null)chapters=new ArrayList<>();
        return chapters;
    }
}