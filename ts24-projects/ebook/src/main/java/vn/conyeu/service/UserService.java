package vn.conyeu.service;

import org.springframework.stereotype.Service;
import vn.conyeu.domain.User;
import vn.conyeu.repository.UserRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class UserService extends LongUIdService<User, UserRepo> {

    public UserService(UserRepo domainRepo) {
        super(domainRepo);
    }
}