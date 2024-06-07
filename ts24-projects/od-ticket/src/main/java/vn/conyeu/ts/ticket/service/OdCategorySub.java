package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsCategorySub;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsSearchReadOption;

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

    public List<ClsCategorySub> search(String parentCate) {
        ObjectMap filter = ObjectMap.setNew("parent_category_id", parentCate);
        return searchRead(new ClsSearchReadOption().applyFilter(filter));
    }

    public List<ClsCategorySub> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsCategorySub> find(ObjectMap searchObj) {
        return searchRead(searchObj);
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