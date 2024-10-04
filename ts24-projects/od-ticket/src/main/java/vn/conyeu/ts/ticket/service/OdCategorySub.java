package vn.conyeu.ts.ticket.service;

import org.springframework.data.domain.Page;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsSearch;
import vn.conyeu.ts.ticket.domain.ClsCategorySub;

import java.util.List;
import java.util.function.Function;

public class OdCategorySub extends OdTicketClient<ClsCategorySub> {
    
    public OdCategorySub(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.subcategory";
    }

//    @Override
//    public String getBasePath() {
//        return "call_kw/"+getModel();
//    }

    public List<ClsCategorySub> getAll() {
        return nameSearch("");
    }

    public Page<ClsCategorySub> search(String parentCate) {
        ObjectMap filter = ObjectMap.setNew("parent_category_id", parentCate);
        return searchRead(ClsSearch.forData(filter));
    }

    @Override
    protected Class<ClsCategorySub> getDomainCls() {
        return ClsCategorySub.class;
    }

    @Override
    protected Function<ObjectMap, ClsCategorySub> mapToObject() {
        return ClsCategorySub::from;
    }
}