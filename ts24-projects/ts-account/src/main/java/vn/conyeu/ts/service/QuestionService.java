package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Question;
import vn.conyeu.ts.repository.QuestionRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class QuestionService extends LongUIdService<Question, QuestionRepo> {

    public QuestionService(QuestionRepo domainRepo) {
        super(domainRepo);
    }
}