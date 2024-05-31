package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsTicketPriority;

import java.util.List;
import java.util.function.Function;

public class OdPriority extends OdTicketCore<ClsTicketPriority> {

    public OdPriority(ClsApiConfig apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.priority";
    }

//    @Override
//    public String getBasePath() {
//        throw new UnsupportedOperationException();
//    }

    @Override
    protected Class<ClsTicketPriority> getDomainCls() {
        return ClsTicketPriority.class;
    }

    @Override
    protected Function<ObjectMap, ClsTicketPriority> mapToObject() {
        return ClsTicketPriority::from;
    }

    public List<ClsTicketPriority> getAll() {
        return searchRead();
    }

    public List<ClsTicketPriority> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsTicketPriority> find(ObjectMap searchObj) {
        return searchRead(searchObj);
    }
}