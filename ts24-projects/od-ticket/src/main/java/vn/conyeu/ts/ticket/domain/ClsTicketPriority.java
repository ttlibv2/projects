package vn.conyeu.ts.ticket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClsTicketPriority extends ClsModel<ClsTicketPriority> {
    Long id;
    String name;
    String display_name;

    public static ClsTicketPriority from(ObjectMap obj) {
        return obj.asObject(ClsTicketPriority.class);
    }

}