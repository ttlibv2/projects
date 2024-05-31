package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.ts.domain.AgColumn;
import vn.conyeu.ts.domain.AgTable;
import vn.conyeu.ts.service.AgTableService;
import vn.conyeu.common.restapi.LongIdRest;

import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/ts.ag-table")
public class AgTableRest extends LongIdRest<AgTable, AgTableService> {

    public AgTableRest(AgTableService service) {
        super(service);
    }

    @GetMapping("get-by-code/{agCode}")
    public AgTable getByCode(@PathVariable String agCode, @RequestParam(defaultValue = "false") Boolean includeCol) {
        AgTable table = service.findByCode(agCode).orElseThrow(() -> new NotFound("noId")
                .message("Bảng không tồn tại").arguments("ag_code", agCode));

        if(!includeCol) {
            table.setColumns(null);
        }

        return table;
    }

}