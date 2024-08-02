package vn.conyeu.ts.ticket.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClsFollow extends ClsModel<ClsFollow> {
    String name;
    String display_name;
    String email;
    Long id;
    Object is_active;
    Object is_editable;
    Long partner_id;
    ObjectMap partner;

    public static ClsFollow from(ObjectMap object) {
        return object.asObject(ClsFollow.class);
    }

    public ClsFollow setPartner(Object partner) {
        this.partner = ClsModel.objectToMap(partner);
        return this;
    }

    public ClsFollow setPartner_id(Object partner_id) {
        if(partner_id instanceof Number num) this.partner_id = num.longValue();
        else this.partner_id = ClsModel.objectToMap(partner_id).getLong("id");
        return this;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        if(partner == null) return null;
        else return partner.getLong("user_id", null);
    }

    /**
     * Returns the partner_id
     */
    public Long getPartner_id() {
        if(partner_id == null && partner != null) return partner.getLong("id");
        return partner_id;
    }
}