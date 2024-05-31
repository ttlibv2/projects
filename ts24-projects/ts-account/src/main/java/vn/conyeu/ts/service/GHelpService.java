package vn.conyeu.ts.service;

import org.springframework.stereotype.Service;
import vn.conyeu.ts.domain.GroupHelp;
import vn.conyeu.ts.repository.GHelpRepo;
import vn.conyeu.common.service.LongIdService;

@Service
public class GHelpService extends LongIdService<GroupHelp, GHelpRepo> {

    public GHelpService(GHelpRepo domainRepo) {
        super(domainRepo);
    }
}