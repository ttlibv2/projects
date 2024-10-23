package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.ts.domain.GroupHelp;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.GroupHelpService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsGroupHelp)
public class GroupHelpRest extends LongUIdRest<GroupHelp, GroupHelpService> {

    public GroupHelpRest(GroupHelpService service) {
        super(service);
    }


}