package vn.conyeu.ts.ticket.service;


import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.ticket.domain.ClsFilterOption;
import vn.conyeu.ts.ticket.domain.ClsMailTemplate;
import vn.conyeu.ts.ticket.domain.ClsNameSearchOption;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OdMailTemplate extends OdTicketClient<ClsMailTemplate> {

    public OdMailTemplate(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "mail.template";
    }

//    @Override
//    public String getBasePath() {
//        return "call_kw/"+getModel();
//    }

    public List<ClsMailTemplate> findAll() {
        return findAll(true);
    }

    public List<ClsMailTemplate> findAll(boolean loadInfo) {
        Object args = new Object[] {"model", "=", "helpdesk.ticket"};
        ClsNameSearchOption option = new ClsNameSearchOption().setArgs(args);
        List<ClsMailTemplate> list = nameSearch("", option);
        if(!loadInfo) return list;
        else return getAllBy(list.stream().map(ClsMailTemplate::getId).toList());
    }

    public Optional<ClsMailTemplate> findById(Long templateId) {
        List<Long> objects = Collections.singletonList(templateId);
        List<ClsMailTemplate> list = getAllBy(objects);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public List<ClsMailTemplate> getAllBy(List<Long> templateId) {
       return read(templateId);
    }


    public List<ClsMailTemplate> find(ClsFilterOption filterOption) {
        return searchRead(filterOption);
    }

    public List<ClsMailTemplate> find(ObjectMap searchObj) {
        return searchRead(searchObj);
    }

    @Override
    protected Class<ClsMailTemplate> getDomainCls() {
        return ClsMailTemplate.class;
    }

    @Override
    protected Function<ObjectMap, ClsMailTemplate> mapToObject() {
        return ClsMailTemplate::from;
    }
}