package vn.conyeu.book.service;

import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.Chapter;
import vn.conyeu.book.repository.ChapterRepo;
import vn.conyeu.common.service.LongIdDateService;

@Service
public class ChapterService extends LongIdDateService<Chapter, ChapterRepo> {

    public ChapterService(ChapterRepo domainRepo) {
        super(domainRepo);
    }
}