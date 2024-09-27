package vn.conyeu.ts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.domain.Template;
import vn.conyeu.ts.repository.TemplateRepo;

import java.util.List;

@Service
//@CacheConfig(cacheNames = "templates")
public class TemplateService extends LongUIdService<Template, TemplateRepo> {

    @Autowired
    public TemplateService(TemplateRepo templateRepo) {
        super(templateRepo);
    }

    @Override
    protected String getCacheName() {
        return "templates";
    }

//    @Cacheable()
    public List<Template> findAll(Long userId) {
        return repo().findTemplateByUser(userId);
    }

//    @Cacheable()
    public Page<Template> findAll(Long userId, List<String> threads, Pageable pageable) {
        if(Objects.isEmpty(threads)) return findAll(userId, pageable);
        else return repo().findTemplateByUser(userId, threads, pageable);
    }

//    @Cacheable()
    public Page<Template> findAll(Long userId, Pageable pageable) {
        return repo().findTemplateByUser(userId, pageable);
    }

//    @Cacheable()
    public Page<Template> findTemplate(String thread, Long userId, Pageable pageable) {
        return repo().findTemplateByUserAndCode(thread, userId, pageable);
    }

    public List<Template> findTemplate(String thread, Long userId) {
        return repo().findTemplateByUserAndCode(thread, userId);
    }

    @Override
    public Template createNew(Template entity) {
        return super.createNew(entity);
    }

    //@CacheEvict(allEntries = true)
    public Template save(Template template) {
        clearCache();
        return super.save(template);
    }

   // @CacheEvict(allEntries = true)
    public void deleteById(Long entityId) {
        super.deleteById(entityId);
        clearCache();
    }

    //@CacheEvict(allEntries = true)
    public void clearCache() {
        cacheService.clearAll();
    }
}