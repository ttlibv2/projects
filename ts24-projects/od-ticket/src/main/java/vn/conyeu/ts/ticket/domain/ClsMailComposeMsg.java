package vn.conyeu.ts.ticket.domain;

import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;
import vn.conyeu.ts.odcore.helper.OdHelper;

@Getter @Setter
public class ClsMailComposeMsg extends ClsModel<ClsMailComposeMsg> {
    Long id;
    String composition_mode = "comment";
    String model = "helpdesk.ticket";
    Long res_id;
    Object is_log = false;
    Object parent_id = false;
    Object mail_server_id = false;
    Object use_active_domain = false;
    String email_from = "no_reply@ts24.com.vn";
    Object partner_ids;
    String subject;
    Object is_wp = false;
    Object body_str = false;
    Object notify = false;
    Object campaign_id = false;
    Object mass_mailing_name = false;
    Object reply_to_force_new = false;
    String reply_to_mode = "update";
    Object reply_to = false;
    String body;

    Object attachment_ids = OdHelper.createList(6, false , new Object[0]);
    Object template_id = 59;


    public void createPartnerId(Long[] partnerIds) {
        this.partner_ids = OdHelper.createList(6, false, partnerIds );
    }

    public static ClsMailComposeMsg from(ObjectMap object) {
        return object.asObject(ClsMailComposeMsg.class);
    }


}