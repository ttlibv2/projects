package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUserContext;
import vn.conyeu.ts.ticket.domain.ClsMailComposeMsg;
import vn.conyeu.ts.ticket.domain.ClsTicketActionReply;

import java.util.function.Function;

/**
 * model: mail.compose.message
 * */
public class OdMailComposeMsg extends OdTicketClient<ClsMailComposeMsg>{

    public OdMailComposeMsg(ClsApiCfg apiConfig) {
        super(apiConfig);
    }

    @Override
    public String getModel() {
        return "mail.compose.message";
    }

    @Override
    protected Class<ClsMailComposeMsg> getDomainCls() {
        return ClsMailComposeMsg.class;
    }

    @Override
    protected Function<ObjectMap, ClsMailComposeMsg> mapToObject() {
        return ClsMailComposeMsg::from;
    }

    public Long create(ClsTicketActionReply reply, ClsMailComposeMsg clsMail) {
        ClsUserContext ctx = createUserContext();
        ClsTicketActionReply.ActionReplyContext clsContext = reply.getContext();
        ctx.getCustom().set(clsContext.cloneMap());
        ctx.set("active_model", clsContext.getDefault_model());
        ctx.set("active_id", clsContext.getDefault_res_id());
        ctx.set("active_ids", new Object[] { clsContext.getDefault_res_id()});
        return ObjectMap.setNew("args", new Object[] { clsMail.cloneMap()})
                .set("model", getModel()).set("method", "create")
                .set("kwargs", ObjectMap.setNew("context", ctx))
                .getLong("result");
    }
}