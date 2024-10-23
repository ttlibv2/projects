package vn.conyeu.ts.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.Question;
import vn.conyeu.ts.repository.QuestionRepo;
import vn.conyeu.common.service.LongUIdService;

import java.util.List;

@Service
@CacheConfig(cacheNames = "questions")
public class QuestionService extends LongUIdService<Question, QuestionRepo> {

    public QuestionService(QuestionRepo domainRepo) {
        super(domainRepo);
    }

    @Cacheable
    public List<Question> findAll() {
        return super.findAll();
    }

    @Cacheable(key = "#pageable")
    public Page<Question> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }

    @Cacheable(key = "{#methodName,#userId,#pageable}")
    public Page<Question> findPageByUser(long userId, Pageable pageable) {
        return repo().findByUser(userId, pageable);
    }

    @Cacheable(key = "{#methodName,#userId}")
    public List<Question> findByUser(long userId) {
        return repo().findByUser(userId);
    }

    @Override
    public Question createNew(Question entity) {
        return super.createNew(entity);
    }

    @Override
    public Question saveAndReturn(Question entity) {
        clearCache();
        return super.saveAndReturn(entity);
    }

    @CacheEvict()
    public void clearCache() {
        super.clearCache();
    }
}