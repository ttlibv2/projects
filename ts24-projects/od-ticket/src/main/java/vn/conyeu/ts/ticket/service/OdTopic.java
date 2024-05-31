package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiConfig;
import vn.conyeu.ts.ticket.domain.ClsTopic;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OdTopic extends OdTicketCore<ClsTopic> {


    public OdTopic(ClsApiConfig apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "helpdesk.topic";
    }

//    @Override
//    public String getBasePath() {
//        return "call_kw/helpdesk.topic";
//    }

    @Override
    protected Class<ClsTopic> getDomainCls() {
        return ClsTopic.class;
    }

    @Override
    protected Function<ObjectMap, ClsTopic> mapToObject() {
        return ClsTopic::from;
    }
    public List<ClsTopic> getAll() {
        return searchRead().stream().peek(c -> {
            c.setCreate_date(null);
            c.setCreate_uid(null);
            c.removeIf("write_date", "__last_update", "write_uid");
        }).collect(Collectors.toList());
    }

}