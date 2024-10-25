package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.restclient.RClient;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/cors-api")
public class React2AngularRest {

    @PostMapping("post")
    public String convert(@RequestBody ObjectMap body) {
        String apiUrl = body.getString("apiUrl");
        ObjectMap headers = body.getMap("headers", new ObjectMap());
        return RClient.builder().baseUrl(apiUrl).build().post()
                .headers(h -> headers.addToHeader(h::put))
                .bodyValue(body.get("payload"))
                .retrieve().bodyToMono(String.class)
                .blockOptional().orElse(null);
    }
}