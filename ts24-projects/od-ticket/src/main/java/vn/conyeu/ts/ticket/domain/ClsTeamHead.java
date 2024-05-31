package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class ClsTeamHead extends ClsModel<ClsTeamHead> {
    Long id;
    String name;

    public static ClsTeamHead from(ObjectMap data) {
        return data.asObject(ClsTeamHead.class);
    }



}