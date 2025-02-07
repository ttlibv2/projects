package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.Attribute;
import vn.conyeu.book.service.AttributeService;
import vn.conyeu.common.restapi.LongIdRest;

@RestController
@RequestMapping("/attribute")
public class AttributeRest extends LongIdRest<Attribute, AttributeService> {

    public AttributeRest(AttributeService service) {
        super(service);
    }

}