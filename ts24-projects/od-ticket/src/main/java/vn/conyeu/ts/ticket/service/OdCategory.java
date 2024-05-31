package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.ticket.domain.ClsCategory;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;

import java.util.List;
import java.util.function.Function;

public class OdCategory extends OdTicketCore<ClsCategory> {
    
    public OdCategory(ClsApiConfig apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.category";
    }

//    @Override
//    public String getBasePath() {
//        return "call_kw/helpdesk.category";
//    }

    @Override
    protected Class<ClsCategory> getDomainCls() {
        return ClsCategory.class;
    }

    @Override
    protected Function<ObjectMap, ClsCategory> mapToObject() {
        return ClsCategory::from;
    }

    public List<ClsCategory> nameSearch(String cateName) {
        return nameSearch(cateName, null);
    }

    public List<ClsCategory> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsCategory> find(ObjectMap searchObj) {
        return searchRead(searchObj);
    }

    public List<ClsCategory> getAll() {
        List<ClsCategory> list = nameSearch("");
        list.removeIf(cls -> cls.getName().equals("CEO"));
        return list;
    }


}