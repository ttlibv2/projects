package vn.conyeu.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter @Setter
public final class CustomPage<T> {
    private List<T> data;

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("total_page")
    private Integer totalPage;

    private Integer limit;

    @JsonProperty("is_last")
    private Boolean isLast;

    @JsonProperty("is_first")
    private Boolean isFirst;


    public static <T> CustomPage<T> from(Page<T> page) {
        CustomPage<T> cp = new CustomPage<>();
        cp.setData(page.getContent());
        cp.setTotalPage(page.getTotalPages());

        if(page instanceof PageImpl imp) {
            cp.setCurrentPage(imp.getNumber());
            cp.setLimit(imp.getSize());
            cp.setIsFirst(imp.isFirst());
            cp.setIsLast(imp.isLast());
        }

        return cp;

    }
}