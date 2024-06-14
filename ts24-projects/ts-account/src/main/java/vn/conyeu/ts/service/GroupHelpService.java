package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.GroupHelp;
import vn.conyeu.ts.repository.GHelpRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class GroupHelpService extends LongUIdService<GroupHelp, GHelpRepo> {

    public GroupHelpService(GHelpRepo domainRepo) {
        super(domainRepo);
    }
}