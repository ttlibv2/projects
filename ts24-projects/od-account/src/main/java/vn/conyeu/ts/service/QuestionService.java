package vn.conyeu.ts.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Question;
import vn.conyeu.ts.repository.QuestionRepo;
import vn.conyeu.common.service.LongUIdService;

import java.util.List;

@Service
public class QuestionService extends LongUIdService<Question, QuestionRepo> {

    public QuestionService(QuestionRepo domainRepo) {
        super(domainRepo);
    }

    //@Cacheable(cacheNames = "questions", key = "#userId")
    public Page<Question> findPageByUser(long userId, Pageable pageable) {
        return repo().findByUser(userId, pageable);
    }

    public List<Question> findByUser(long userId) {
        return repo().findByUser(userId);
    }

    @Override
    public Question createNew(Question entity) {
        return super.createNew(entity);
    }
}