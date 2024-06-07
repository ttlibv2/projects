package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsSubjectType;

import java.util.List;
import java.util.function.Function;

/** Ticket Subject Type */
public class OdTicketSubType extends OdTicketClient<ClsSubjectType> {

    public OdTicketSubType(ClsApiCfg apiConfig) {
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
    protected Class<ClsSubjectType> getDomainCls() {
        return null;
    }

    @Override
    protected Function<ObjectMap, ClsSubjectType> mapToObject() {
        return null;
    }
    public List<ClsSubjectType> nameSearch() {
        return nameSearch("");
    }

    public List<ClsSubjectType> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsSubjectType> find(ObjectMap searchObj) {
        return searchRead(searchObj);
    }


    public List<ClsSubjectType> getAll() {
        return nameSearch();
    }
}