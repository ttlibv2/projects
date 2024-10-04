package vn.conyeu.ts.ticket.service;

import org.springframework.data.domain.Page;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.ticket.domain.ClsWkTeam;

import java.util.function.Function;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
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

    public Page<ClsWkTeam> getAll() {
        return searchRead();
    }

}