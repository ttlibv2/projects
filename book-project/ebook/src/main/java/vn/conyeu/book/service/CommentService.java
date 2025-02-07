package vn.conyeu.book.service;

import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.Comment;
import vn.conyeu.book.repository.CommentRepo;
import vn.conyeu.common.service.LongIdDateService;

@Service
public class CommentService extends LongIdDateService<Comment, CommentRepo> {

    public CommentService(CommentRepo domainRepo) {
        super(domainRepo);
    }
}