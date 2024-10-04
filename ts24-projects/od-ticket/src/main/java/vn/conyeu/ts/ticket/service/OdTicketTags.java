package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.ticket.domain.ClsTicketTag;

import java.util.function.Function;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
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