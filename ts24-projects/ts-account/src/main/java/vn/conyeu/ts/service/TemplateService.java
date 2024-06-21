package vn.conyeu.ts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.ts.domain.Template;
import vn.conyeu.ts.repository.TemplateRepo;

import java.util.List;

@Service
public class TemplateService extends LongUIdService<Template, TemplateRepo> {

    @Autowired
    public TemplateService(TemplateRepo templateRepo) {
        super(templateRepo);
    }

    public List<Template> findAll(Long userId) {
        return repo().findTemplateByUser(userId);
    }

    public List<Template> findAll(Long userId, List<String> entityCodes) {
        if(entityCodes == null || entityCodes.isEmpty()) return findAll(userId);
        else return repo().findTemplateByUser(userId, entityCodes);
    }

    public Page<Template> findAll(Long userId, Pageable pageable) {
        return repo().findTemplateByUser(userId, pageable);
    }

    public Page<Template> findTemplate(String entityCode, Long userId, Pageable pageable) {
        return repo().findTemplateByUserAndCode(entityCode, userId, pageable);
    }
}