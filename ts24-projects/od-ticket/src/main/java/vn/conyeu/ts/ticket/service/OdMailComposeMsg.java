package vn.conyeu.ts.ticket.service;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsApiCfg;
import vn.conyeu.ts.odcore.domain.ClsUserContext;
import vn.conyeu.ts.ticket.domain.ClsMailComposeMsg;
import vn.conyeu.ts.ticket.domain.ClsTicketActionReply;

import java.util.List;
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

    public ClsMailComposeMsg create(ClsTicketActionReply reply, ClsMailComposeMsg clsMail) {
        ClsTicketActionReply.ActionReplyContext clsContext = reply.getContext();
        ClsUserContext ctx = createUserContext();
        ctx.getCustom().set(clsContext.cloneMap());
        ctx.set("active_model", clsContext.getDefault_model());
        ctx.set("active_id", clsContext.getDefault_res_id());
        ctx.set("active_ids", new Object[] { clsContext.getDefault_res_id()});

        ObjectMap body = ObjectMap.setNew("args", new Object[] { clsMail.cloneMap()})
                .set("model", getModel()).set("method", "create")
                .set("kwargs", ObjectMap.setNew("context", ctx));

        // create message
        Long messageId = sendPost(body, call_kwUri("create")).getLong("result");

        // callbutton send
        callButton("action_send_mail", List.of(messageId), context -> context.set(ctx.cloneMap()));

        // return
        clsMail.setId(messageId);
        clsMail.setSendContext(ctx);
        return clsMail;
    }


}