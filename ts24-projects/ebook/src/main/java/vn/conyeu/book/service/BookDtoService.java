package vn.conyeu.book.service;

import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.BookDto;
import vn.conyeu.book.repository.BookDtoRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class BookDtoService extends LongUIdService<BookDto, BookDtoRepo> {

    public BookDtoService(BookDtoRepo domainRepo) {
        super(domainRepo);
    }
}