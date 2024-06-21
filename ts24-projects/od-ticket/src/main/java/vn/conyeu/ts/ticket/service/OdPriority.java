package vn.conyeu.ts.ticket.service;

import org.springframework.data.domain.Page;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsTicketPriority;

import java.util.function.Function;

public class OdPriority extends OdTicketClient<ClsTicketPriority> {

    public OdPriority(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.priority";
    }

    @Override
    protected Class<ClsTicketPriority> getDomainCls() {
        return ClsTicketPriority.class;
    }

    @Override
    protected Function<ObjectMap, ClsTicketPriority> mapToObject() {
        return ClsTicketPriority::from;
    }


}