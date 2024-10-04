package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.ticket.domain.ClsStage;

import java.util.function.Function;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
public class OdStage extends OdTicketClient<ClsStage> {

    public OdStage(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.stages";
    }
    
    @Override
    protected Class<ClsStage> getDomainCls() {
        return ClsStage.class;
    }

    @Override
    protected Function<ObjectMap, ClsStage> mapToObject() {
        return ClsStage::from;
    }

  
}