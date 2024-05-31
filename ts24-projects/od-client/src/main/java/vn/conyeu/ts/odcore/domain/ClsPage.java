package vn.conyeu.ts.odcore.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.utils.Objects;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class ClsPage extends ClsModel {
    Integer limit = 80;
    Boolean sort;

    public ClsPage withLimit(int pageSize) {
        setLimit(pageSize);
        return this;
    }

    public void setDefaultLimit(int limit) {
        if(Objects.isLoe(this.limit, 0)) this.limit = limit;
    }
}