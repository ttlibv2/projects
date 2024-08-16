package vn.conyeu.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.conyeu.book.domain.ChapterDto;
import vn.conyeu.common.repository.LongUIdRepo;

public interface ChapterDtoRepo extends LongUIdRepo<ChapterDto> {

    @Query(value = "select e from #{#entityName} e where e.book.id=?1 and e.getHtml=false",
        countQuery = "select count(e) from #{#entityName} e where e.book.id=?1 and e.getHtml=false")
    Page<ChapterDto> findExtractHtml(Long bookId, Pageable pageable);

    @Query(value = "select e from #{#entityName} e where e.book.id=?1 and e.getHtml=true and e.saveHtml=false",
            countQuery = "select count(e) from #{#entityName} e where e.book.id=?1 and e.getHtml=true and e.saveHtml=false")
    Page<ChapterDto> findSaveHtml(Long bookId, Pageable pageable);
}