package vn.conyeu.ts.ticket_rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.odcore.domain.ClsUser;
import vn.conyeu.ts.service.OdService;
import vn.conyeu.ts.service.UserApiService;
import vn.conyeu.ts.ticket.domain.*;
import vn.conyeu.ts.ticket.service.OdTicketService;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.odCatalog)
public class OdCatalogRest extends OdBaseRest {

    public OdCatalogRest(OdService odService, UserApiService apiService) {
        super(odService, apiService);
    }

    @GetMapping("get-all")
    public ObjectMap getAll(@RequestParam String include) {
        include = Objects.isBlank(include) ? "all" : include.toLowerCase().trim();

        final OdTicketService service = service();
        final boolean isAll = include.equalsIgnoreCase("all");
        final List<String> segments = List.of(include.split(","));

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
                    : categories.stream().map(cate -> service.categorySub().search(cate.getName()))
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

        if (isAll || segments.contains("ls_repiled_status")) {
            map.set("ls_repiled_status", getRepiledStatus());
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
        ClsUser cls = service.getConfig().getClsUser();
        map.set(service.getUniqueId()+".user_api", new ClsUser().setId(cls.getId())
                .setUser_name(cls.getUser_name())
                .setDisplay_name(cls.getDisplay_name())
                .setEmail(cls.getEmail())
        );


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
        OdTicketService service = service();
        List<ClsHelpdeskTeam> helpdeskTeams = service.team().find(filterOption);

        Long odUserId = service.getConfig().getClsUser().getId();

        for (ClsHelpdeskTeam helpdeskTeam : helpdeskTeams) {
            List<Long> members = helpdeskTeam.getListTeam_members();
            List<ClsUser> clsUsers = searchUsersByIds(service, members);

            int index = IntStream.range(0, clsUsers.size())
                    .filter(i -> clsUsers.get(i).getId().equals(odUserId))
                    .findFirst().orElse(-1);

            if(index != -1) {
                ClsUser clsUser = clsUsers.remove(index);
                clsUsers.add(0, clsUser);
            }

            helpdeskTeam.set("ls_team_member", clsUsers);
        }
        return helpdeskTeams;
    }

    @GetMapping("get-product")
    public List<ClsProduct> getAllProduct(@RequestParam Integer size) {
        return service().product().getAll(size);
    }

    // @GetMapping("get-user-by-ids")
    private List<ClsUser> searchUsersByIds(OdTicketService service, List<Long> userIds) {
        return service.user().search(userIds);
    }

}