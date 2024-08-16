package vn.conyeu.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.ChapterDto;
import vn.conyeu.book.repository.ChapterDtoRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class ChapterDtoService extends LongUIdService<ChapterDto, ChapterDtoRepo> {

    public ChapterDtoService(ChapterDtoRepo domainRepo) {
        super(domainRepo);
    }

    public Page<ChapterDto> findExtractHtml(Long bookId, Pageable pageable) {
        return repo().findExtractHtml(bookId, pageable);
    }

    public Page<ChapterDto> findSaveHtml(Long bookId, Pageable pageable) {
        return repo().findSaveHtml(bookId, pageable);
    }
}