package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsStage;

import java.util.List;
import java.util.function.Function;

public class OdStage extends OdTicketCore<ClsStage> {

    public OdStage(ClsApiConfig apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.stages";
    }

//    @Override
//    public String getBasePath() {
//        return "";
//    }
    
    @Override
    protected Class<ClsStage> getDomainCls() {
        return ClsStage.class;
    }

    @Override
    protected Function<ObjectMap, ClsStage> mapToObject() {
        return ClsStage::from;
    }
    
    public List<ClsStage> getAll() {
        return searchRead();
    }

    public List<ClsStage> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsStage> find(ObjectMap searchObj) {
        return searchRead(searchObj);
    }

  
}