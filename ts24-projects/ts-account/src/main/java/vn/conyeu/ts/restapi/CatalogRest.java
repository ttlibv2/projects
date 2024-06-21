package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.Template;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.*;
import vn.conyeu.ts.ticket_rest.OdCatalogRest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public CatalogRest(ChanelService chanelService, SoftwareService softwareService, GroupHelpService groupHelpService, QuestionService questionService, OdCatalogRest odCatalogRest, TemplateService templateService) {
        this.chanelService = chanelService;
        this.softwareService = softwareService;
        this.groupHelpService = groupHelpService;
        this.questionService = questionService;
        this.odCatalogRest = odCatalogRest;
        this.templateService = templateService;
    }

    @GetMapping("/get-all")
    public ObjectMap getAll(@PrincipalId long userId, @RequestParam Map<String, Object> params) {
        ObjectMap mapParam = ObjectMap.fromMap(params);

        String include = mapParam.getString("include", "all");
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

        if(isAll || segments.contains("ls_teamplate")) {
            String codeStr = mapParam.getString("entities", null);
            List<String> codes = codeStr == null ? null : List.of(codeStr.split(","));
            List<Template> templates = templateService.findAll(userId, codes);
            data.set("ls_teamplate", templates.parallelStream()
                    .collect(Collectors.toMap(Template::getEntityCode, t -> t)));
        }

        return data;

    }
}