package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.User;
import vn.conyeu.book.service.UserService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping("/bookuser")
public class BookUserRest extends LongUIdRest<User, UserService> {

    public BookUserRest(UserService service) {
        super(service);
    }

}