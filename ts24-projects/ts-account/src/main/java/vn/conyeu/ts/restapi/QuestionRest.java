package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.ts.domain.Question;
import vn.conyeu.ts.service.QuestionService;
import vn.conyeu.common.restapi.LongIdRest;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/ts.question")
public class QuestionRest extends LongIdRest<Question, QuestionService> {

    public QuestionRest(QuestionService service) {
        super(service);
    }

}