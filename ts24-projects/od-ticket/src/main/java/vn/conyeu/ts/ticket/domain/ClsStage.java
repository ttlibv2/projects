package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Getter @EqualsAndHashCode(callSuper = false)
public class ClsStage extends ClsModel<ClsStage> {
    private Integer id;
    private String name;
    private String display_name;

    public static ClsStage from(Object stage_id) {
        if(stage_id == null) return null;
        else return from(ClsModel.objectToMap(stage_id));
    }

    public static ClsStage ifNull(ClsStage cls) {
        return cls == null ? new ClsStage():cls;
    }

    public static ClsStage from(ObjectMap obj) {
        return obj.asObject(ClsStage.class);
    }

    /**
     * Set the id
     *
     * @param id the value
     */
    public ClsStage setId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Set the name
     *
     * @param name the value
     */
    public ClsStage setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the display_name
     *
     * @param display_name the value
     */
    public ClsStage setDisplay_name(String display_name) {
        this.display_name = display_name;
        return this;
    }




}