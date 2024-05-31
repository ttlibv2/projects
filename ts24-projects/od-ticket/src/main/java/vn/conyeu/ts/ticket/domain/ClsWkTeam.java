package vn.conyeu.ts.ticket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Data
@EqualsAndHashCode(callSuper = false, of = "id")
public  class ClsWkTeam extends ClsModel<ClsWkTeam> {
    Long id;
    String name;
    String display_name;
    Object[] child_ids;
    Object[] create_uid;
    Object department_id;
    String description;
    Object[] manager;
    Long[] members;
    String team_email;

    public static ClsWkTeam from(ObjectMap obj) {
        return obj.asObject(ClsWkTeam.class);
    }

    @Override
    public String toString() {
        return getDisplay_name();
    }

}