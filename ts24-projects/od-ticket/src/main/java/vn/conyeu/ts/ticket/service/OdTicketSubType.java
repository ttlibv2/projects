package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.ticket.domain.ClsSubjectType;

import java.util.List;
import java.util.function.Function;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
/** Ticket Subject Type */
public class OdTicketSubType extends OdTicketClient<ClsSubjectType> {

    public OdTicketSubType(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.sub.type";
    }

    @Override
    protected Class<ClsSubjectType> getDomainCls() {
        return ClsSubjectType.class;
    }

    @Override
    protected Function<ObjectMap, ClsSubjectType> mapToObject() {
        return ClsSubjectType::from;
    }

    public List<ClsSubjectType> nameSearch() {
        return nameSearch("");
    }

    public List<ClsSubjectType> getAll() {
        return nameSearch();
    }
}