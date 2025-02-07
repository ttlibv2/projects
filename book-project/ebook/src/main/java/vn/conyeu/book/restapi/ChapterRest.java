package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.Chapter;
import vn.conyeu.book.service.ChapterService;
import vn.conyeu.common.restapi.LongIdDateRest;

@RestController
@RequestMapping("/chapter")
public class ChapterRest extends LongIdDateRest<Chapter, ChapterService> {

    public ChapterRest(ChapterService service) {
        super(service);
    }

}