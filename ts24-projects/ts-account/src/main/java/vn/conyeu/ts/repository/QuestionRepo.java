package vn.conyeu.ts.repository;

import org.springframework.data.jpa.repository.Query;
import vn.conyeu.ts.domain.Question;
import vn.conyeu.common.repository.LongUIdRepo;

import java.util.List;

public interface QuestionRepo extends LongUIdRepo<Question> {

    @Query("select e from #{#entityName} e where (e.user.id=?1 or e.shared=true)")
    List<Question> findByUser(long userId);
}