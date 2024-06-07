package vn.conyeu.ts.ticket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Data @EqualsAndHashCode(callSuper = false)
public class ClsSubjectType extends ClsModel<ClsSubjectType> {
    Integer id;
    String name;
    String display_name;

    public static ClsSubjectType assign(ObjectMap obj) {
        return obj.asObject(ClsSubjectType.class);
    }

    public static ClsSubjectType from(Object subject_id) {
        if(subject_id == null) return null;
        else return ClsSubjectType.assign(ClsModel.objectToMap(subject_id));
    }

}