package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;
import java.util.Map;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class ClsRepiledStatus extends ClsModel<ClsRepiledStatus> {
    Integer id;
    String name;
    String display_name;
    String code;

    public ClsRepiledStatus() {
    }

    public ClsRepiledStatus(Integer id, String code, String name) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public static ClsRepiledStatus from(ObjectMap obj) {
        return obj.asObject(ClsRepiledStatus.class);
    }
    public static ClsRepiledStatus from(String code) {
        return ALL.get(code);
    }

    public final static Map<String, ClsRepiledStatus> ALL = Map.of(
            "staff_replied",new ClsRepiledStatus(1, "staff_replied", "Staff Replied"),
            "customer_replied", new ClsRepiledStatus(2, "customer_replied", "Customer Replied")
    );

}