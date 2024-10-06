package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;
import vn.conyeu.ts.odcore.domain.ClsPage;
import vn.conyeu.ts.odcore.domain.ClsSearch;

import java.util.List;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public  class ClsSearchReadOption extends ClsModel<ClsSearchReadOption> {
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

    public ClsSearchReadOption(ClsSearch cls) {
        this(new ClsFilterOption(cls), cls.newPage());
    }

    public ClsPage getPage() {
        page.setDefaultLimit(80);
        return page;
    }

    public ClsSearchReadOption applyFilter(ClsSearch cls) {
        filterOption.resetWith(cls.getOperator(), cls.getData());
        return this;
    }

    public List<Object> buildFilter() {
        return filterOption.build();
    }
}