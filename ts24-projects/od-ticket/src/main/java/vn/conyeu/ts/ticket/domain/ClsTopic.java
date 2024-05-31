package vn.conyeu.ts.ticket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Data
@EqualsAndHashCode(callSuper = false)
public  class ClsTopic extends ClsModel<ClsTopic> {
    Long id;
    Long sequence;
    String name;
    String display_name;
    Object[] category_id;
    String create_date;
    Object[] create_uid;

    @Override
    public String toString() {
        return getDisplay_name();
    }

    public static ClsTopic from(ObjectMap obj) {
        return obj.asObject(ClsTopic.class);
    }

}