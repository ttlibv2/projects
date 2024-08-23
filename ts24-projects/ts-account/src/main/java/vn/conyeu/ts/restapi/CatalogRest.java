package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.common.service.ICacheService;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.Template;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.*;
import vn.conyeu.ts.restapi.odrest.OdCatalogRest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsCatalog)
public class CatalogRest {
    final ChanelService chanelService;
    final SoftwareService softwareService;
    final GroupHelpService groupHelpService;
    final QuestionService questionService;
    final OdCatalogRest odCatalogRest;
    final TemplateService templateService;
    final ICacheService cacheService;

    public CatalogRest(ChanelService chanelService, SoftwareService softwareService, GroupHelpService groupHelpService, QuestionService questionService, OdCatalogRest odCatalogRest, TemplateService templateService,ICacheService cacheService) {
        this.chanelService = chanelService;
        this.softwareService = softwareService;
        this.groupHelpService = groupHelpService;
        this.questionService = questionService;
        this.odCatalogRest = odCatalogRest;
        this.templateService = templateService;
        this.cacheService = cacheService;
    }

    @GetMapping("clear-cache")
    public Object clearCache() {
        cacheService.clearAll();

        return ObjectMap.setNew("alert_msg", "Xoa cache xong")
                .set("result", cacheService.getCacheNames());
    }

    @GetMapping("/get-all")
    public ObjectMap getAll(@PrincipalId long userId, @RequestParam Map<String, Object> params) {
        ObjectMap mapParam = ObjectMap.fromMap(params);

        String include = mapParam.getString("catalog", "all");
        ObjectMap data = odCatalogRest.getAll(include);

        final boolean isAll = include.equalsIgnoreCase("all");
        final List<String> segments = List.of(include.split(","));

        if(isAll || segments.contains("ls_chanel")) {
            data.set("ls_chanel", chanelService.findAll());
        }

        if(isAll || segments.contains("ls_software")) {
            data.set("ls_software", softwareService.findAll());
        }

        if(isAll || segments.contains("ls_group_help")) {
            data.set("ls_group_help", groupHelpService.findAll());
        }

        if(isAll || segments.contains("ls_question")) {
            data.set("ls_question", questionService.findByUser(userId));
        }

        if(isAll || segments.contains("ls_template")) {
            String codeStr = mapParam.getString("entities", null);
            List<String> codes = codeStr == null ? null : List.of(codeStr.split(","));
            List<Template> templates = templateService.findAll(userId, codes);

            MultiValueMap<String, Template> valueMap = new LinkedMultiValueMap<>();
            for(Template template:templates) {
                String entityCode = template.getThread();
                valueMap.computeIfAbsent(entityCode, k -> new ArrayList<>()).add(template);
            }

            data.set("ls_template", valueMap);
        }

        return data;

    }
}