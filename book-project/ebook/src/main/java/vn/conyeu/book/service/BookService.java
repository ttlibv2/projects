package vn.conyeu.book.service;

import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.Book;
import vn.conyeu.book.repository.BookRepo;
import vn.conyeu.common.service.LongIdDateService;

@Service
public class BookService extends LongIdDateService<Book, BookRepo> {

    public BookService(BookRepo domainRepo) {
        super(domainRepo);
    }
}