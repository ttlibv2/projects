package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.Comment;
import vn.conyeu.book.service.CommentService;
import vn.conyeu.common.restapi.LongIdDateRest;

@RestController
@RequestMapping("/comment")
public class CommentRest extends LongIdDateRest<Comment, CommentService> {

    public CommentRest(CommentService service) {
        super(service);
    }

}