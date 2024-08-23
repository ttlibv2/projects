package vn.conyeu.ts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.conyeu.common.repository.LongUIdRepo;
import vn.conyeu.ts.domain.Template;

import java.util.List;

public interface TemplateRepo extends LongUIdRepo<Template> {

    @Query(value = "select e from #{#entityName} e where e.user.id=?2 and e.thread=?1",
    countQuery = "select count(e) from #{#entityName} e where e.user.id=?2 and e.thread=?1")
    Page<Template> findTemplateByUserAndCode(String thread, Long userId, Pageable pageable);

    @Query(value = "select e from #{#entityName} e where e.user.id=?1 ",
            countQuery = "select count(e) from #{#entityName} e where e.user.id=?1 ")
    Page<Template> findTemplateByUser(Long userId, Pageable pageable);

    @Query("select e from #{#entityName} e where e.user.id=?1 ")
    List<Template> findTemplateByUser(Long userId);

    @Query("select e from #{#entityName} e where e.user.id=?1 and e.thread in ?2")
    List<Template> findTemplateByUser(Long userId, List<String> threads);

}