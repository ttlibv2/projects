package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.Book;
import vn.conyeu.book.service.BookService;
import vn.conyeu.common.restapi.LongIdDateRest;

@RestController
@RequestMapping("/book")
public class BookRest extends LongIdDateRest<Book, BookService> {

    public BookRest(BookService service) {
        super(service);
    }

}