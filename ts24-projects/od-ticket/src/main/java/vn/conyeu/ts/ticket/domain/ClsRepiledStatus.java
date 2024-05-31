package vn.conyeu.ts.ticket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
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

    public static List<ClsRepiledStatus> createListDefault() {
       return ALL.values().stream().toList();
    }

    public static ClsRepiledStatus from(String code) {
        return ALL.get(code);
    }


    private static Map<String, ClsRepiledStatus> ALL;
    static  {
        ALL = new LinkedHashMap<>();
        ALL.put("staff_replied",new ClsRepiledStatus(1, "staff_replied", "Staff Replied"));
        ALL.put("customer_replied", new ClsRepiledStatus(2, "customer_replied", "Customer Replied"));
    }
}