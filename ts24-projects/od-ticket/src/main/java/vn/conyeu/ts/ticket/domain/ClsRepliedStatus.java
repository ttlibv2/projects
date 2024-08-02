package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;
import java.util.Map;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class ClsRepliedStatus extends ClsModel<ClsRepliedStatus> {
    Integer id;
    String name;
    String display_name;
    String code;

    public ClsRepliedStatus() {
    }

    public ClsRepliedStatus(Integer id, String code, String name) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public static ClsRepliedStatus from(ObjectMap obj) {
        return obj.asObject(ClsRepliedStatus.class);
    }
    public static ClsRepliedStatus from(String code) {
        return ALL.get(code);
    }

    public final static Map<String, ClsRepliedStatus> ALL = Map.of(
            "staff_replied",new ClsRepliedStatus(1, "staff_replied", "Staff Replied"),
            "customer_replied", new ClsRepliedStatus(2, "customer_replied", "Customer Replied")
    );

}