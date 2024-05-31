package vn.conyeu.ts.restapi;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.domain.AgColumn;
import vn.conyeu.ts.service.AgColumnService;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/ts.ag-column")
public class AgColumnRest {
    private final AgColumnService columnService;

    public AgColumnRest(AgColumnService columnService) {
        this.columnService = columnService;
    }

    @GetMapping("get-by-tableId/{tableId}")
    public List<AgColumn> getByTableId(@PathVariable Long tableId) {
        return columnService.findByTableId(tableId);
    }

    @GetMapping("get-by-tableCode/{tableCode}")
    public List<AgColumn> getByTableId(@PathVariable String tableCode) {
        return columnService.findByTableCode(tableCode);
    }

    @PostMapping("edit-by-id/{colId}")
    public AgColumn editColumnById(@PathVariable Long colId, @RequestBody ObjectMap mapBody) {
        return columnService.update(colId, mapBody).orElseThrow(() -> columnService.noId(colId));
    }

    @GetMapping("get-by-id/{colId}")
    public AgColumn getById(@PathVariable Long colId) {
        return columnService.getById(colId);
    }

    @DeleteMapping("delete-by-id/{colId}")
    public void deleteById(@PathVariable Long colId) {
        columnService.deleteById(colId);
    }


}