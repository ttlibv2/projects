package vn.conyeu.ts.ticket.service;

import org.springframework.data.domain.Page;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsTicketTag;

import java.util.function.Function;

public class OdTicketTags extends OdTicketClient<ClsTicketTag> {

    public OdTicketTags(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.tags";
    }

    @Override
    protected Class<ClsTicketTag> getDomainCls() {
        return ClsTicketTag.class;
    }

    @Override
    protected Function<ObjectMap, ClsTicketTag> mapToObject() {
        return ClsTicketTag::from;
    }
}