package vn.conyeu.ts.ticket.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsHelper;
import vn.conyeu.ts.odcore.domain.ClsModel;

import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = false)
public  class ClsPartner extends ClsModel<ClsPartner> {
    Long id;

    @NotBlank(message = "clsPartner.name.notEmpty")
    String name;

    String display_name;

    @NotBlank(message = "clsPartner.email.notEmpty")
    String email;

    Boolean is_company = false;
    String phone;
    String mobile;
    String street;
    String type = "contact";
    String vat;
    Boolean active = false;
    String company_type;
    Object parent_id;

    Long comp_id;
    String comp_name;

    String customer_name;
    Long customer_id;
    Long user_id;

    public String getPhone() {
        return Objects.isBlank(phone) ? mobile : phone;
    }

    public String getMobile() {
        return Objects.isBlank(mobile) ? phone : mobile;
    }

    public ClsPartner setUser_id(Object user_id) {
        if(!isNull(user_id)) this.user_id = ClsHelper.getObj(user_id, 1, Long.class);
        return this;
    }

    public static ClsPartner from(ObjectMap obj) {
        return obj.asObject(ClsPartner.class);
    }

    public static ClsPartner from(Object partner_id) {
        if(partner_id == null) return null;
        else return from(ClsModel.objectToMap(partner_id));
    }

    public Optional<ClsPartner> asOptional() {
        return Optional.of(this);
    }

}