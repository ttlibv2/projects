package vn.conyeu.book.service;

import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.User;
import vn.conyeu.book.repository.UserRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class UserService extends LongUIdService<User, UserRepo> {

    public UserService(UserRepo domainRepo) {
        super(domainRepo);
    }
}