package vn.conyeu.ts.ticket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Data @EqualsAndHashCode(callSuper = false)
public class ClsHelpdeskSubType extends ClsModel<ClsHelpdeskSubType> {
    Integer id;
    String name;
    String display_name;

    public static ClsHelpdeskSubType assign(ObjectMap obj) {
        return obj.asObject(ClsHelpdeskSubType.class);
    }

    public static ClsHelpdeskSubType from(Object subject_id) {
        if(subject_id == null) return null;
        else return ClsHelpdeskSubType.assign(ClsModel.objectToMap(subject_id));
    }

}