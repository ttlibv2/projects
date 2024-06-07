package vn.conyeu.ts.ticket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;
@Data @EqualsAndHashCode(callSuper = false)
public class ClsProduct extends ClsModel<ClsProduct> {


    public static ClsProduct from(Object category_id) {
        if (category_id == null) return null;
        else return from(ClsModel.objectToMap(category_id));
    }

    public static ClsProduct from(ObjectMap obj) {
        return obj.asObject(ClsProduct.class);
    }


}