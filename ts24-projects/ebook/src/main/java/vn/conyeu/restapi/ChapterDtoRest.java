package vn.conyeu.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.domain.ChapterDto;
import vn.conyeu.service.ChapterDtoService;
import vn.conyeu.common.restapi.LongUIdRest;

@RestController
@RequestMapping("/chapterdto")
public class ChapterDtoRest extends LongUIdRest<ChapterDto, ChapterDtoService> {

    public ChapterDtoRest(ChapterDtoService service) {
        super(service);
    }

}