package vn.conyeu.ts.service;

import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(cacheNames = "questions", key = "#userId")
    public List<Question> findByUser(long userId) {
        return repo().findByUser(userId);
    }
}