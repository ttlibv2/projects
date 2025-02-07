package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.SeoAlias;
import vn.conyeu.book.service.SeoAliasService;
import vn.conyeu.common.restapi.LongIdRest;

@RestController
@RequestMapping("/seoalias")
public class SeoAliasRest extends LongIdRest<SeoAlias, SeoAliasService> {

    public SeoAliasRest(SeoAliasService service) {
        super(service);
    }

}