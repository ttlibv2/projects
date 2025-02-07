package vn.conyeu.book.service;

import org.springframework.stereotype.Service;
import vn.conyeu.book.domain.Category;
import vn.conyeu.book.repository.CategoryRepo;
import vn.conyeu.common.service.LongIdDateService;

@Service
public class CategoryService extends LongIdDateService<Category, CategoryRepo> {

    public CategoryService(CategoryRepo domainRepo) {
        super(domainRepo);
    }
}