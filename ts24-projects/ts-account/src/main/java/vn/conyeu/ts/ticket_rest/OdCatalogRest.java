package vn.conyeu.ts.ticket_rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.ticket.domain.*;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.util.Collection;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.odCatalog)
public class OdCatalogRest extends OdBaseRest {

    public OdCatalogRest(OdService odService, UserApiService apiService) {
        super(odService, apiService);
    }

    @GetMapping("get-all")
    public Object catalog_getAll(@RequestParam String include) {
        include = Objects.isBlank(include) ? "all" : include.toLowerCase().trim();

        final OdTicketService service = service();
        final boolean isAll = include.equalsIgnoreCase("all");
        final List<String> segments = List.of(include.split(","));
        final ObjectMap map = new ObjectMap();

        if(isAll || segments.contains("ticket_type")) {
            map.set("ticket_type", getAllTicketType());
        }

        if(isAll || segments.contains("team")) {
            map.set("team", getAllHelpdeskTeam());
        }

        List<ClsCategory> categories = null;
        if(isAll || segments.contains("catagory")) {
            categories =  getAllCate();
            map.set("catagory", categories);
        }


        if(isAll || segments.contains("catagory_sub")) {
            List<ClsCategorySub> subList = categories == null ? getAllCateSub()
                    : categories.stream().map(cate -> service.categorySub().search(cate.getName()))
                    .flatMap(Collection::stream).toList();

            map.set("catagory_sub",subList);
        }

        if(isAll || segments.contains("tags")) {
            map.set("tags", getAllTicketTags());
        }

        if(isAll || segments.contains("priority")) {
            map.set("priority", getAllTicketPriority());
        }

        if(isAll || segments.contains("products")) {
            map.set("products", getAllProduct());
        }

        if(isAll || segments.contains("subject_type")) {
            map.set("subject_type", getSubjecType());
        }

        if(isAll || segments.contains("stage")) {
            map.set("stage", getAllStage());
        }

        if(isAll || segments.contains("repiled_status")) {
            map.set("repiled_status", getRepiledStatus());
        }

        return map;
    }


    @GetMapping("get-category")
    public List<ClsCategory> getAllCate() {
        ClsFilterOption filterOption = new ClsFilterOption().NotLike("name", "ceo");
        return service().category().find(filterOption);
    }

    @GetMapping("get-category-sub")
    public List<ClsCategorySub> getAllCateSub() {
        ClsFilterOption filterOption = new ClsFilterOption().NotLike("parent_category_id", "ceo");
        return service().categorySub().find(filterOption);
    }

    @GetMapping("get-category-sub/{parentSub}")
    public List<ClsCategorySub> getAllCateSubByParent(@PathVariable String parentSub) {
        return service().categorySub().search(parentSub);
    }

    @GetMapping("get-team")
    public List<ClsWkTeam> getAllWkTeam() {
        return service().wkTeam().getAll();
    }

    @GetMapping("get-topic")
    public List<ClsTopic> getAllTopic() {
        return service().topic().getAll();
    }


    @GetMapping("get-ticket-subject-type")
    public List<ClsSubjectType> getSubjecType() {
        return service().ticketSubjectType().nameSearch();
    }

    @GetMapping("get-repiled-status")
    public Collection<ClsRepiledStatus> getRepiledStatus() {
        return ClsRepiledStatus.ALL.values();
    }

    @GetMapping("get-ticket-tags")
    public List<ClsTicketTag> getAllTicketTags() {
        return service().ticketTags().getAll();
    }

    @GetMapping("get-ticket-priority")
    public List<ClsTicketPriority> getAllTicketPriority() {
        return service().priority().getAll();
    }

    @GetMapping("get-stage")
    public List<ClsStage> getAllStage() {
        return service().stage().getAll();
    }

    @GetMapping("get-ticket-type")
    public List<ClsTicketType> getAllTicketType() {
        return service().ticketType().getAll();
    }

    @GetMapping("get-helpdesk-team")
    public List<ClsHelpdeskTeam> getAllHelpdeskTeam() {
        ClsFilterOption filterOption = new ClsFilterOption();
        filterOption.NotLike("team_email", "CEO@ts24.com.vn");
        return service().team().find(filterOption);
    }

    @GetMapping("get-product")
    public List<ClsProduct> getAllProduct() {
        return service().product().getAll();
    }

}