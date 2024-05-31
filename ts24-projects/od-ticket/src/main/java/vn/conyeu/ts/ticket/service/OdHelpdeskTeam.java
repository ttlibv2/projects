package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsHelpdeskTeam;

import java.util.List;
import java.util.function.Function;

import java.util.Arrays;

public class OdHelpdeskTeam extends OdTicketCore<ClsHelpdeskTeam> {

    public OdHelpdeskTeam(ClsApiConfig apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.team";
    }

//    @Override
//    public String getBasePath() {
//        return "call_kw/"+getModel();
//    }

    public List<ClsHelpdeskTeam> findAll() {
       return findAll(true);
    }

    public List<ClsHelpdeskTeam> findAll(boolean isGetDetail) {
        List<ClsHelpdeskTeam> list = nameSearch("");
        return !isGetDetail ? list : readBy(list.stream().map(ClsHelpdeskTeam::getId).toList());
    }

    /**
     * Run method: call_kw/helpdesk.team/read
     * @param teamIds list helpdesk team id
     * */
    public List<ClsHelpdeskTeam> readBy(List<Integer> teamIds) {
        Object fields = getFields("read", getModel());
        return read(Arrays.asList(teamIds, fields));
    }

    public List<ClsHelpdeskTeam> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsHelpdeskTeam> find(ObjectMap searchObj) {
        return searchRead(searchObj);
    }


    @Override
    protected Class<ClsHelpdeskTeam> getDomainCls() {
        return ClsHelpdeskTeam.class;
    }

    @Override
    protected Function<ObjectMap, ClsHelpdeskTeam> mapToObject() {
        return ClsHelpdeskTeam::from;
    }
}