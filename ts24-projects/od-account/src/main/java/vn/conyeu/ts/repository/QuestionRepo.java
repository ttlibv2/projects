package vn.conyeu.ts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.conyeu.common.repository.LongUIdRepo;
import vn.conyeu.ts.domain.Question;

import java.util.List;

public interface QuestionRepo extends LongUIdRepo<Question> {

    @Query(value = "select e from #{#entityName} e where (e.user.id=?1 or e.shared=true)",
            countQuery = "select count(e) from #{#entityName} e where (e.user.id=?1 or e.shared=true)")
    Page<Question> findByUser(long userId, Pageable pageable);

    @Query(value = "select e from #{#entityName} e where (e.user.id=?1 or e.shared=true)")
    List<Question> findByUser(long userId);
}