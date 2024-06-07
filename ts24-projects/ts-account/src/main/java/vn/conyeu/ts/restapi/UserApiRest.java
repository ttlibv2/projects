package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.ts.dtocls.TsVar;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsUserApi)
public class UserApiRest {

}