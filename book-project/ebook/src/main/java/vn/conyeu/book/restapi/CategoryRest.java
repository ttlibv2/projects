package vn.conyeu.book.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.conyeu.book.domain.Category;
import vn.conyeu.book.service.CategoryService;
import vn.conyeu.common.restapi.LongIdDateRest;

@RestController
@RequestMapping("/category")
public class CategoryRest extends LongIdDateRest<Category, CategoryService> {

    public CategoryRest(CategoryService service) {
        super(service);
    }

}