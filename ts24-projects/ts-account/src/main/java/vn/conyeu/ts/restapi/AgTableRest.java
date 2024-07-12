package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.AgTable;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.AgTableService;
import vn.conyeu.common.restapi.LongUIdRest;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsAgTable)
public class AgTableRest extends LongUIdRest<AgTable, AgTableService> {

    public AgTableRest(AgTableService service) {
        super(service);
    }

    @GetMapping("get-by-code")
    public AgTable getByCode(@RequestParam String code, @PrincipalId Long userId) {
        AgTable agTable = service.findByCode(code).orElseThrow(() -> new NotFound("noId")
                .message("Bảng không tồn tại").arguments("ag_code", code));

        List<AgTable> children = service.findByParentId(agTable.getId());
        agTable.setChildren(children);

        return agTable;
    }

}