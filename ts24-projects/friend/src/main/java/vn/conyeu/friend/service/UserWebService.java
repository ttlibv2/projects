package vn.conyeu.friend.service;

import org.springframework.stereotype.Service;
import vn.conyeu.friend.domain.UserWeb;
import vn.conyeu.friend.repository.UserWebRepo;
import vn.conyeu.common.service.LongIdService;

import java.util.Optional;

@Service
public class UserWebService extends LongIdService<UserWeb, UserWebRepo> {

    public UserWebService(UserWebRepo domainRepo) {
        super(domainRepo);
    }

    public Optional<UserWeb> findByWebId(String domain, Object webId) {
        return repo().findByWebId(domain + "::" + webId.toString());
    }
}