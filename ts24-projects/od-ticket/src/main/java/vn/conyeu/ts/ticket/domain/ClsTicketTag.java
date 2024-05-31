package vn.conyeu.ts.ticket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClsTicketTag extends ClsModel<ClsTicketTag> {
    Long id;
    String name;
    String display_name;

    public static ClsTicketTag from(ObjectMap obj) {
        return obj.asObject(ClsTicketTag.class);
    }

}