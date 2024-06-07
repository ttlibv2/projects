package vn.conyeu.ts.ticket.domain;

import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Getter @Setter
public class ClsTicketActionReply extends ClsModel<ClsTicketActionReply> {
    String res_model;
    String target;
    String view_mode;
    Long view_id;
    Object views; // [ [ view_id, view_mode]]
    ActionReplyContext context;

    public static ClsTicketActionReply from(ObjectMap map) {
        ClsTicketActionReply reply = map.asObject(ClsTicketActionReply.class);
        reply.context = ActionReplyContext.from(map.getMap("context"));
        return reply;
    }

    @Getter @Setter
    public static class ActionReplyContext extends ClsModel<ActionReplyContext> {
        String default_composition_mode;
        String default_model;
        Long default_res_id;
        Long default_template_id;
        Boolean default_use_template;
        Boolean force_email;

        static ActionReplyContext from(ObjectMap map) {
            return map.asObject(ActionReplyContext.class);
        }
    }
}