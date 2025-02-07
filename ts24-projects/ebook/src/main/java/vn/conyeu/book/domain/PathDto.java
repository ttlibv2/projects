package vn.conyeu.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.conyeu.common.domain.LongUId;
@Entity @Table(name="eb_book")
@Getter() @Setter @NoArgsConstructor
@DynamicInsert @DynamicUpdate
@JsonIgnoreProperties({"author"})
@AttributeOverride(name = "id", column = @Column(name = "bookId"))
//@formatter:on
public class PathDto extends LongUId<PathDto> {

    @Column(length = 100)
    private String source;

    @Column(length = 150)
    private String keyword;

    @Column(length = 350)
    private String link;

    @ColumnDefault("0")
    private Integer totalPage;

    @ColumnDefault("0")
    private Integer currentPage;
}