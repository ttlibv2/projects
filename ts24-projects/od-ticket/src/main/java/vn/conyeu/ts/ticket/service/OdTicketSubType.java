package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsHelpdeskSubType;

import java.util.List;
import java.util.function.Function;

/** Ticket Subject Type */
public class OdTicketSubType extends OdTicketCore<ClsHelpdeskSubType> {

    public OdTicketSubType(ClsApiConfig apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.sub.type";
    }

//    @Override
//    public String getBasePath() {
//        return "call_kw/"+getModel();
//    }

    @Override
    protected Class<ClsHelpdeskSubType> getDomainCls() {
        return null;
    }

    @Override
    protected Function<ObjectMap, ClsHelpdeskSubType> mapToObject() {
        return null;
    }
    public List<ClsHelpdeskSubType> nameSearch() {
        return nameSearch("");
    }

    public List<ClsHelpdeskSubType> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsHelpdeskSubType> find(ObjectMap searchObj) {
        return searchRead(searchObj);
    }


    public List<ClsHelpdeskSubType> getAll() {
        return nameSearch();
    }
}