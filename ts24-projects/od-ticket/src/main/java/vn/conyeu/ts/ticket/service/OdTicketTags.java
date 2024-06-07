package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsTicketTag;

import java.util.List;
import java.util.function.Function;

public class OdTicketTags extends OdTicketClient<ClsTicketTag> {

    public OdTicketTags(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.tags";
    }

//    @Override
//    public String getBasePath() {
//        throw new UnsupportedOperationException();
//    }

    @Override
    protected Class<ClsTicketTag> getDomainCls() {
        return ClsTicketTag.class;
    }

    @Override
    protected Function<ObjectMap, ClsTicketTag> mapToObject() {
        return ClsTicketTag::from;
    }
    
    public List<ClsTicketTag> getAll() {
       return searchRead();
    }

    public List<ClsTicketTag> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsTicketTag> find(ObjectMap searchObj) {
        return searchRead(searchObj);
    }

   
}