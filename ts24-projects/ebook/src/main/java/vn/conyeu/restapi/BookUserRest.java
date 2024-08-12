package vn.conyeu.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.domain.User;
import vn.conyeu.service.UserService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping("/bookuser")
public class BookUserRest extends LongUIdRest<User, UserService> {

    public BookUserRest(UserService service) {
        super(service);
    }

}