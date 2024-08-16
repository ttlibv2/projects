package vn.conyeu.book.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;

//@formatter:off
@Entity @Table(name="eb_chapter_dto")
@Getter @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@AttributeOverride(name = "id", column = @Column(name = "chapterId"))
//@formatter:on
public class ChapterDto extends LongUId<ChapterDto> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId", nullable = false)
    private BookDto book;

    private String title;

    private String seoUrl;
    private String fullUrl;

    @Column(columnDefinition = "longtext")
    private String contentHtml;

    @ColumnDefault("0")
    private Boolean getHtml;

    @ColumnDefault("0")
    private Boolean saveHtml;

    @Column(length = 3000)
    private String logError;


}