package vn.conyeu.ts.restapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.identity.annotation.PrincipalId;
import vn.conyeu.ts.domain.Question;
import vn.conyeu.ts.domain.TsUser;
import vn.conyeu.ts.dtocls.TsVar;
import vn.conyeu.ts.service.QuestionService;

import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(TsVar.Rest.tsQuestion)
public class QuestionRest {//extends LongUIdRest<Question, QuestionService> {
    private final QuestionService service;
    public QuestionRest(QuestionService service) {
        this.service = service;
    }

    @GetMapping("search")
    public Page<Question> getAll(@RequestParam Map<String, Object> params, Pageable pageable) {
        return service.findAll(ObjectMap.fromMap(params), pageable);
    }

    @PostAuthorize("hasRole('ADMIN')")
    @GetMapping("get-all")
    public Page<Question> getAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("get-by-user-login")
    public Page<Question> getAllByUser(@PrincipalId Long userId, Pageable pageable) {
        return service.findPageByUser(userId, pageable);
    }

    @GetMapping("get-by-id/{questionId}")
    public Question getById(@PathVariable Long questionId) {
        return service.findById(questionId).orElseThrow(() -> service.noId(questionId));
    }

    @PutMapping("update-by-id/{questionId}")
    public Question updateById(@RequestBody Map<String, ?> body, @PathVariable Long questionId) {
        ObjectMap object = ObjectMap.fromMap(body);
        return service.update(questionId, object).orElseThrow(() -> service.noId(questionId));
    }

    @DeleteMapping("delete-by-id/{questionId}")
    public Object deleteById(@PathVariable Long questionId) {
        boolean bool = service.existsById(questionId);
        if(!bool) throw service.noId(questionId);

        service.deleteById(questionId);
        return ObjectMap.setNew("model_id", questionId)
                .set("alert_msg", "Đã xóa thành công dữ liệu");
    }

    @PostMapping("create-new")
    public Question createObject(@PrincipalId Long userId, @RequestBody Question object) {
        object.setUser(new TsUser(userId));
        return service.createNew(object);
    }
    

}