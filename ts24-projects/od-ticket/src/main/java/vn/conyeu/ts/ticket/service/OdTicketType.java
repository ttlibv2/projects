package vn.conyeu.ts.ticket.service;

import org.springframework.data.domain.Page;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsTicketType;

import java.util.List;
import java.util.function.Function;

public class OdTicketType extends OdTicketClient<ClsTicketType> {

    public OdTicketType(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.ticket.type";
    }

    @Override
    protected Class<ClsTicketType> getDomainCls() {
        return ClsTicketType.class;
    }

    @Override
    protected Function<ObjectMap, ClsTicketType> mapToObject() {
        return ClsTicketType::from;
    }

    public final Page<ClsTicketType> findAll() {
       return forPage(nameSearch(""));
    }

    public List<ClsTicketType> getAll() {
        return findAll().getContent();
    }
}