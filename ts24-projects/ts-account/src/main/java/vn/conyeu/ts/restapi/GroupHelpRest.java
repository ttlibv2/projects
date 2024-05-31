package vn.conyeu.ts.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.ts.domain.GroupHelp;
import vn.conyeu.ts.service.GHelpService;
import vn.conyeu.common.restapi.LongIdRest;

@RestController
@RequestMapping("/ts.group-help")
public class GroupHelpRest extends LongIdRest<GroupHelp, GHelpService> {

    public GroupHelpRest(GHelpService service) {
        super(service);
    }

}