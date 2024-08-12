package vn.conyeu.service;

import org.springframework.stereotype.Service;
import vn.conyeu.domain.BookDto;
import vn.conyeu.repository.BookDtoRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class BookDtoService extends LongUIdService<BookDto, BookDtoRepo> {

    public BookDtoService(BookDtoRepo domainRepo) {
        super(domainRepo);
    }
}