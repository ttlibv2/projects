package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.BookDto;
import vn.conyeu.book.service.BookDtoService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping("/bookdto")
public class BookDtoRest extends LongUIdRest<BookDto, BookDtoService> {

    public BookDtoRest(BookDtoService service) {
        super(service);
    }

}