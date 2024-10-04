package vn.conyeu.ts.restapi.odrest;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.ticket.domain.*;

import java.util.Collection;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.odCatalog)
@CacheConfig(cacheNames = "od_catalogs")
public class OdCatalogRest extends OdBaseRest {

    public OdCatalogRest(OdService odService, UserApiService apiService) {
        super(odService, apiService);
    }

    @GetMapping("get-all")
    @Cacheable(key = "'get-all'+'-'+#catalog")
    public ObjectMap getAll(@RequestParam String catalog) {
        catalog = Objects.isBlank(catalog) ? "all" : catalog.toLowerCase().trim();

        //final OdTicketService service = tsApp();
        final boolean isAll = catalog.equalsIgnoreCase("all");
        final List<String> segments = List.of(catalog.split(","));

        final ObjectMap map = new ObjectMap();

        List<ClsHelpdeskTeam> helpdeskTeams = null;

        if (isAll || segments.contains("ls_ticket_type")) {
            map.set("ls_ticket_type", getAllTicketType());
        }

        if (isAll || segments.contains("ls_helpdesk_team")) {
            helpdeskTeams = getAllHelpdeskTeam();
            map.set("ls_helpdesk_team", helpdeskTeams);
        }

        List<ClsCategory> categories = null;
        if (isAll || segments.contains("ls_category")) {
            categories = getAllCate();
            map.set("ls_category", categories);
        }

        if (isAll || segments.contains("ls_category_sub")) {
            List<ClsCategorySub> subList = categories == null ? getAllCateSub()
                    : categories.stream().map(cate -> getAllCateSubByParent(cate.getName()))
                    .flatMap(Collection::stream).toList();

            map.set("ls_category_sub", subList);
        }

        if (isAll || segments.contains("ls_ticket_tag")) {
            map.set("ls_ticket_tag", getAllTicketTags());
        }

        if (isAll || segments.contains("ls_priority")) {
            map.set("ls_priority", getAllTicketPriority());
        }

        if (isAll || segments.contains("ls_product")) {
            map.set("ls_product", getAllProduct(10));
        }

        if (isAll || segments.contains("ls_subject_type")) {
            map.set("ls_subject_type", getSubjecType());
        }

        if (isAll || segments.contains("ls_stage")) {
            map.set("ls_stage", getAllStage());
        }

        if (isAll || segments.contains("ls_replied_status")) {
            map.set("ls_replied_status", getRepiledStatus());
        }

        if (isAll || segments.contains("ls_topic")) {
            // map.set("ls_topic", getAllTopic());
        }

//        if(isAll || segments.contains("ls_assign")) {
//           if(helpdeskTeams != null) {
//               for(ClsHelpdeskTeam helpdeskTeam:helpdeskTeams) {
//                   List<Long> members = helpdeskTeam.getListTeam_members();
//                   helpdeskTeam.set("ls_team_member", searchUsersByIds(members));
//               }
//           }
//        }

        //
        ClsApiCfg cfg = tsApp().config();
        ClsUser cls = cfg.getClsUser();
        map.set(cfg.getAppName(), new ClsUser().setId(cls.getId())
                .setUser_name(cls.getUser_name())
                .setDisplay_name(cls.getDisplay_name())
                .setEmail(cls.getEmail())
        );


        return map;
    }


    @GetMapping("get-category")
    public List<ClsCategory> getAllCate() {
        ClsFilterOption filterOption = new ClsFilterOption().NotLike("name", "ceo");
        return tsApp().category().find(filterOption).getContent();
    }

    @GetMapping("get-category-sub")
    public List<ClsCategorySub> getAllCateSub() {
        ClsFilterOption filterOption = new ClsFilterOption().NotLike("parent_category_id", "ceo");
        return tsApp().categorySub().find(filterOption).getContent();
    }

    @GetMapping("get-category-sub/{parentSub}")
    public List<ClsCategorySub> getAllCateSubByParent(@PathVariable String parentSub) {
        return tsApp().categorySub().search(parentSub).getContent();
    }

    @GetMapping("get-team")
    public List<ClsWkTeam> getAllWkTeam() {
        return tsApp().wkTeam().getAll().getContent();
    }

    @GetMapping("get-topic")
    public List<ClsTopic> getAllTopic() {
        return tsApp().topic().getAll();
    }


    @GetMapping("get-ticket-subject-type")
    public List<ClsSubjectType> getSubjecType() {
        return tsApp().ticketSubjectType().nameSearch();
    }

    @GetMapping("get-repiled-status")
    public Collection<ClsRepliedStatus> getRepiledStatus() {
        return ClsRepliedStatus.ALL.values();
    }

    @GetMapping("get-ticket-tags")
    public List<ClsTicketTag> getAllTicketTags() {
        return tsApp().ticketTags().findAll().getContent();
    }

    @GetMapping("get-ticket-priority")
    public List<ClsTicketPriority> getAllTicketPriority() {
        return tsApp().priority().findAll().getContent();
    }

    @GetMapping("get-stage")
    public List<ClsStage> getAllStage() {
        return tsApp().stage().findAll().getContent();
    }

    @GetMapping("get-ticket-type")
    @Cacheable(key = "'get-ticket-type'")
    public List<ClsTicketType> getAllTicketType() {
        return tsApp().ticketType().getAll();
    }

    @GetMapping("get-helpdesk-team")
    public List<ClsHelpdeskTeam> getAllHelpdeskTeam() {
        ClsFilterOption filterOption = new ClsFilterOption();
        filterOption.NotLike("team_email", "CEO@ts24.com.vn");
        return tsApp().team().find(filterOption).getContent();
    }

    @GetMapping("get-product")
    public List<ClsProduct> getAllProduct(@RequestParam Integer size) {
        return tsApp().product().getAll(size);
    }

//    // @GetMapping("get-user-by-ids")
//    private List<ClsUser> searchUsersByIds0(OdTicketService service, List<Long> userIds) {
//        return service.user().search(userIds);
//    }

}