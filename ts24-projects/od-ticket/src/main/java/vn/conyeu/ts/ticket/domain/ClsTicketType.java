package vn.conyeu.ts.ticket.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClsTicketType extends ClsModel<ClsTicketType> {
    Integer id;
    String name;
    String display_name;

    public static ClsTicketType from(ObjectMap obj) {
        return obj.asObject(ClsTicketType.class);
    }

    public static ClsTicketType from(Object ticket_type) {
        if(ticket_type == null) return null;
        else return from(ClsModel.objectToMap(ticket_type));
    }


}