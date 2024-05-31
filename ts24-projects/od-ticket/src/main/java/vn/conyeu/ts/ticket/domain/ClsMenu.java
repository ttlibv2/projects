package vn.conyeu.ts.ticket.domain;

import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

public class ClsMenu extends ClsModel<ClsMenu> {

    public static ClsMenu from(ObjectMap objectMap) {
        return objectMap.asObject(ClsMenu.class);
    }

}