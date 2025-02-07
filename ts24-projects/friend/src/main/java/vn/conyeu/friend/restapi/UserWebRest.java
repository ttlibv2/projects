package vn.conyeu.friend.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.friend.domain.UserWeb;
import vn.conyeu.friend.service.UserWebService;
import vn.conyeu.common.restapi.LongIdRest;

@RestController
@RequestMapping("/userdto")
public class UserWebRest extends LongIdRest<UserWeb, UserWebService> {

    public UserWebRest(UserWebService service) {
        super(service);
    }

}