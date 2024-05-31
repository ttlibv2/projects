package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;
import vn.conyeu.ts.odcore.domain.ClsPage;

import java.util.List;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public  class ClsSearchReadOption extends ClsModel {
    private final ObjectMap context = ObjectMap.create();
    private final ClsFilterOption filterOption;
    private final ClsPage page;

    public ClsSearchReadOption() {
        this(new ClsFilterOption(), new ClsPage());
    }

    public ClsSearchReadOption(ClsFilterOption filterOption) {
       this(filterOption, new ClsPage());
    }

    public ClsSearchReadOption(ClsFilterOption filterOption, ClsPage page) {
        this.filterOption = filterOption;
        this.page = page;
    }

    public ClsSearchReadOption(ObjectMap dataFilter, ClsPage page) {
        this(new ClsFilterOption(), page);
        this.applyFilter(dataFilter);
    }

    public ClsSearchReadOption(ObjectMap dataFilter) {
        this(dataFilter, new ClsPage());
    }


    public ClsPage getPage() {
        page.setDefaultLimit(80);
        return page;
    }

    public ClsSearchReadOption applyFilter(ObjectMap data) {
        ObjectMap newMap = data.copy().delete("size");
        filterOption.resetWith(ClsOperator.Like, newMap);
        return this;
    }

    public List<Object> buildFilter() {
        return filterOption.build();
    }
}