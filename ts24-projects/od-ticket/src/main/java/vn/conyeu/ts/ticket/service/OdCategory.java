package vn.conyeu.ts.ticket.service;

import org.springframework.data.domain.Page;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsCategory;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;

import java.util.List;
import java.util.function.Function;

public class OdCategory extends OdTicketClient<ClsCategory> {
    
    public OdCategory(ClsApiCfg apiConfig) {
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




    public List<ClsCategory> getAll() {
        List<ClsCategory> list = nameSearch("");
        list.removeIf(cls -> cls.getName().equals("CEO"));
        return list;
    }


}