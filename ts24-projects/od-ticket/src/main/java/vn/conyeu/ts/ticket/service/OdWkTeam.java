package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsWkTeam;

import java.util.List;
import java.util.function.Function;

public class OdWkTeam extends OdTicketClient<ClsWkTeam> {

    public OdWkTeam(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "wk.team";
    }

    public String getBasePath() {
        return "call_kw/wk.team";
    }

    @Override
    protected Class<ClsWkTeam> getDomainCls() {
        return ClsWkTeam.class;
    }

    @Override
    protected Function<ObjectMap, ClsWkTeam> mapToObject() {
        return ClsWkTeam::from;
    }
    public List<ClsWkTeam> getAll() {
        return searchRead();
    }

}