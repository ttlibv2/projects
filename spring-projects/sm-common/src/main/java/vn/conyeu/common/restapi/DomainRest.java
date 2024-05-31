package vn.conyeu.common.restapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.common.domain.DomainId;
import vn.conyeu.common.exception.BaseException;
import vn.conyeu.common.exception.NotFound;
import vn.conyeu.common.service.DomainService;
import vn.conyeu.commons.beans.ObjectMap;

import java.io.Serializable;

public abstract class DomainRest<E extends DomainId<E, Id>, Id extends Serializable, S extends DomainService<E, Id, ?>> {
    protected final S service;

    public DomainRest(S service) {
        this.service = service;
    }

    protected BaseException noId(Id entityId) {
        return new NotFound().code("noId")
                .message("Không tồn tại dữ liệu với ID=%s", entityId);
    }

    @GetMapping("search")
    public Page<E> getAll(@RequestParam ObjectMap params, Pageable pageable) {
        return service.findAll(params, pageable);
    }

    @GetMapping("get-all")
    public Page<E> getAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("get-by-id/{entityId}")
    public E getById(@PathVariable Id entityId) {
        return service.findById(entityId).orElseThrow(() -> noId(entityId));
    }

    @PutMapping("update-by-id/{entityId}")
    public E updateById(@RequestBody ObjectMap object, @PathVariable Id entityId) {
        return service.update(entityId, object).orElseThrow(() -> noId(entityId));
    }

    @DeleteMapping("delete-by-id/{entityId}")
    public boolean deleteById(@PathVariable Id entityId) {
        boolean bool = service.existsById(entityId);
        if(!bool) throw noId(entityId);

        service.deleteById(entityId);
        return true;
    }

    @PostMapping("create-new")
    public E createObject(@RequestBody E object) {
        return service.createNew(object);
    }
}