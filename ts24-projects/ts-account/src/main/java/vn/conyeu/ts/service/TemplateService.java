package vn.conyeu.ts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.conyeu.common.exception.BadRequest;
import vn.conyeu.common.service.LongUIdService;
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
    public List<Template> findAll(Long userId, List<String> entityCodes) {
        if(entityCodes == null || entityCodes.isEmpty()) return findAll(userId);
        else return repo().findTemplateByUser(userId, entityCodes);
    }

//    @Cacheable()
    public Page<Template> findAll(Long userId, Pageable pageable) {
        return repo().findTemplateByUser(userId, pageable);
    }

//    @Cacheable()
    public Page<Template> findTemplate(String entityCode, Long userId, Pageable pageable) {
        return repo().findTemplateByUserAndCode(entityCode, userId, pageable);
    }

    @Override
    public Template createNew(Template entity) {
        boolean hasTitle = existsByTitle(entity.getUserId(), entity.getTitle(), entity.getEntityCode());
        if(hasTitle) throw new BadRequest("e_title").message("Tieu de da ton tai");
        return super.createNew(entity);
    }

    public boolean existsByTitle(Long userId, String entity, String title) {
        return repo().existsByTitle(userId, entity, title);
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