package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsCategory;

import java.util.List;
import java.util.function.Function;

public class OdCategory extends OdTicketClient<ClsCategory> {
    public OdCategory(ClsApiCfg cfg) {
        super(cfg);
    }

    public String getModel() {
        return "helpdesk.category";
    }

    protected Class<ClsCategory> getDomainCls() {
        return ClsCategory.class;
    }

    protected Function<ObjectMap, ClsCategory> mapToObject() {
        return ClsCategory::from;
    }

    public List<ClsCategory> nameSearch(String cateName) {
        return nameSearch(cateName, null);
    }

    public List<ClsCategory> getAll() {
        List<ClsCategory> list = nameSearch("");
        list.removeIf(cls -> cls.getName().equals("CEO"));
        return list;
    }


}