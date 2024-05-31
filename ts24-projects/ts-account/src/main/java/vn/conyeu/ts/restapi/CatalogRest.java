package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.Chanel;
import vn.conyeu.ts.domain.GroupHelp;
import vn.conyeu.ts.domain.Question;
import vn.conyeu.ts.domain.Software;
import vn.conyeu.ts.service.*;
import vn.conyeu.ts.ticket.domain.ClsCategory;
import vn.conyeu.ts.ticket.domain.ClsCategorySub;
import vn.conyeu.ts.ticket.domain.ClsRepiledStatus;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
@PreAuthorize("isAuthenticated()")
public class CatalogRest {
    final ChanelService chanelService;
    final QuestionService questionService;
    final SoftwareService softwareService;
    final GHelpService gHelpService;

    public CatalogRest(ChanelService chanelService, QuestionService questionService,
                       SoftwareService softwareService, GHelpService gHelpService) {
        this.chanelService = chanelService;
        this.questionService = questionService;
        this.softwareService = softwareService;
        this.gHelpService = gHelpService;
    }

    @GetMapping("chanels/get-all")
    public List<Chanel> getChanels() {
        return chanelService.findAll();
    }

    @GetMapping("questions/get-all")
    public List<Question> getQuestions() {
        return questionService.findAll();
    }

    @GetMapping("softwares/get-all")
    public List<Software> getSoftwares() {
        return softwareService.findAll();
    }

    @GetMapping("ghelps/get-all")
    public List<GroupHelp> getGroupHelps() {
        return gHelpService.findAll();
    }

    @GetMapping("get-all")
    public ObjectMap getCatalogs(@RequestParam String include, @PrincipalId Long userId) {

        include = include.toLowerCase().trim();

        List<String> actions = List.of(include.split(","));
        boolean all = actions.contains("all");
        ObjectMap obj = ObjectMap.create();

        if(all || actions.contains("chanel")) {
            obj.put("chanels", getChanels());
        }

        if(all || actions.contains("question")) {
            obj.put("questions",getQuestions());
        }

        if(all || actions.contains("software")) {
            obj.put("softwares", getSoftwares());
        }

        if(all || actions.contains("group_help")) {
            obj.put("group_helps", getGroupHelps());
        }

        //-- odoo category
        if(all || include.equals("odoo") || include.contains("od_")) {
            OdTicketService service = OdService.forUser(userId).ticketService();

            if(all || actions.contains("od_team")) {
                obj.set("od_team", service.team().findAll());
            }

            if(all || actions.contains("od_ticket_type")) {
                obj.set("od_ticket_type", service.ticketType().getAll());
            }

            if(all || actions.contains("od_ticket_subtype")) {
                obj.set("od_ticket_subtype", service.ticketSubType().getAll());
            }

            if(all || actions.contains("od_tags")) {
                obj.set("od_tags", service.ticketTags().getAll());
            }

            if(all || actions.contains("od_ticket_priority")) {
                obj.set("od_ticket_priority", service.priority().getAll());
            }

            if(all || actions.contains("od_repiled_status")) {
                obj.set("od_repiled_status", ClsRepiledStatus.createListDefault());
            }

            List<ClsCategory> categories = null;
            if(all || actions.contains("od_category")) {
                categories = service.category().getAll();
                obj.set("od_category", categories);
            }

            if(all || actions.contains("od_category_sub")) {
                List<ClsCategorySub> subList = categories == null ? service.categorySub().getAll()
                        : categories.stream().map(p -> service.categorySub().search(p.getName()))
                        .flatMap(List::stream).distinct().toList();

                obj.set("od_category_sub", subList);
            }

        }

        return obj;
    }



}