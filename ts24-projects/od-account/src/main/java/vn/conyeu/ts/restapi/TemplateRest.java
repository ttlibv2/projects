package vn.conyeu.ts.restapi;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.restapi.LongUIdRest;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.Template;
import vn.conyeu.ts.domain.TsUser;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.TemplateService;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsTicketTemplate)
public class TemplateRest extends LongUIdRest<Template, TemplateService> {

    public TemplateRest(TemplateService service) {
        super(service);
    }

    @GetMapping(value = "/user/get-all")
    public Page<Template> getAll(@PrincipalId Long userId, @RequestParam(required = false) String threads, Pageable pageable) {
        List<String> list = Objects.isBlank(threads) ? null : List.of(threads.split(","));
        return service.findAll(userId, list, pageable);
    }

    @PostMapping("save")
    public Template save(@PrincipalId Long userId, @RequestBody @Valid Template template) {
        template.setUser(new TsUser(userId));

        Long templateId = template.getId();
        if(templateId == null) return service.createNew(template);
        else return service.update(templateId, template).orElseThrow(() -> service.noId(templateId));
    }

}