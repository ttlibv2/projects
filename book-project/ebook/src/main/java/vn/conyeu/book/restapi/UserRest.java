package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.User;
import vn.conyeu.book.service.UserService;
import vn.conyeu.common.restapi.LongIdDateRest;

@RestController
@RequestMapping("/user")
public class UserRest extends LongIdDateRest<User, UserService> {

    public UserRest(UserService service) {
        super(service);
    }

}