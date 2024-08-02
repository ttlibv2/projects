package vn.conyeu.ts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.conyeu.common.service.LongUIdService;
import vn.conyeu.ts.domain.AttachFile;
import vn.conyeu.ts.repository.AttachFileRepo;

@Service
public class AttachFileService extends LongUIdService<AttachFile, AttachFileRepo> {

    @Autowired
    public AttachFileService(AttachFileRepo fileRepo) {
        super(fileRepo);
    }
}