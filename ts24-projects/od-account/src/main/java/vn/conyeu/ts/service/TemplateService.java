package vn.conyeu.ts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.domain.Template;
import vn.conyeu.ts.repository.TemplateRepo;

import java.util.List;

@Service
public class TemplateService extends LongUIdService<Template, TemplateRepo> {

    @Autowired
    public TemplateService(TemplateRepo templateRepo) {
        super(templateRepo);
    }

    @Override
    protected String getCacheName() {
        return "templates";
    }

    public List<Template> findAll(Long userId) {
        return repo().findTemplateByUser(userId);
    }

    @Cacheable(cacheNames = "templates", key = "{#methodName,#userId,#threads,#pageable}")
    public Page<Template> findAll(Long userId, List<String> threads, Pageable pageable) {
        if (Objects.isEmpty(threads)) return findAll(userId, pageable);
        else return repo().findTemplateByUser(userId, threads, pageable);
    }

    @Cacheable(cacheNames = "templates", key = "{#methodName,#userId,#pageable}")
    public Page<Template> findAll(Long userId, Pageable pageable) {
        return repo().findTemplateByUser(userId, pageable);
    }

    @Cacheable(cacheNames = "templates", key = "{#methodName,#userId,#thread,#pageable}")
    public Page<Template> findTemplate(String thread, Long userId, Pageable pageable) {
        return repo().findTemplateByUserAndCode(thread, userId, pageable);
    }

    @Cacheable(cacheNames = "templates", key = "{#methodName,#userId,#thread}")
    public List<Template> findTemplate(String thread, Long userId) {
        return repo().findTemplateByUserAndCode(thread, userId);
    }

    @Override
    public Template createNew(Template entity) {
        return super.createNew(entity);
    }

    public Template saveAndReturn(Template template) {
        clearCache();
        return super.saveAndReturn(template);
    }

    public void deleteById(Long entityId) {
        super.deleteById(entityId);
        clearCache();
    }

    @CacheEvict(cacheNames = "templates")
    public void clearCache() {
        cacheService.clearAll();
    }

}