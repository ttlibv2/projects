package vn.conyeu.service;

import org.springframework.stereotype.Service;
import vn.conyeu.domain.ChapterDto;
import vn.conyeu.repository.ChapterDtoRepo;
import vn.conyeu.common.service.LongUIdService;

@Service
public class ChapterDtoService extends LongUIdService<ChapterDto, ChapterDtoRepo> {

    public ChapterDtoService(ChapterDtoRepo domainRepo) {
        super(domainRepo);
    }
}