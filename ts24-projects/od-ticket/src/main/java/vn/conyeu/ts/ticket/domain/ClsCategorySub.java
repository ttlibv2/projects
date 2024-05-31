package vn.conyeu.ts.ticket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Data

@EqualsAndHashCode(callSuper = false)
public class ClsCategorySub extends ClsModel<ClsCategorySub> {
    private Long id;
    private Long sequence;
    private String name;
    private String display_name;
    private Object create_uid;
    private String create_date;
    private String write_date;
    private Object parent_category_id;
    private Object team_id; // object[] | map
    private Object write_uid; // object[] | map

    public static ClsCategorySub from(ObjectMap map) {
        map.delete("__last_update");
        ClsCategorySub sub = map.asObject(ClsCategorySub.class);
        sub.team_id = ClsModel.objectToMap(map.get("team_id"));
        sub.write_uid = ClsModel.objectToMap(map.get("write_uid"));
        sub.parent_category_id = ClsModel.objectToMap(map.get("parent_category_id"));
        sub.create_uid = ClsModel.objectToMap(map.get("create_uid"));
        return sub;
    }

    public static ClsCategorySub from(Object category_id) {
        if (category_id == null) return null;
        else return from(ClsModel.objectToMap(category_id));
    }

    /**
     * Set the id
     *
     * @param id the value
     */
    public ClsCategorySub setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Set the sequence
     *
     * @param sequence the value
     */
    public ClsCategorySub setSequence(Long sequence) {
        this.sequence = sequence;
        return this;
    }

    /**
     * Set the name
     *
     * @param name the value
     */
    public ClsCategorySub setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the display_name
     *
     * @param display_name the value
     */
    public ClsCategorySub setDisplay_name(String display_name) {
        this.display_name = display_name;
        return this;
    }

    /**
     * Set the create_uid
     *
     * @param create_uid the value
     */
    public ClsCategorySub setCreate_uid(Object create_uid) {
        this.create_uid = create_uid;
        return this;
    }

    /**
     * Set the create_date
     *
     * @param create_date the value
     */
    public ClsCategorySub setCreate_date(String create_date) {
        this.create_date = create_date;
        return this;
    }

    /**
     * Set the write_date
     *
     * @param write_date the value
     */
    public ClsCategorySub setWrite_date(String write_date) {
        this.write_date = write_date;
        return this;
    }

    /**
     * Set the parent_category_id
     *
     * @param parent_category_id the value
     */
    public ClsCategorySub setParent_category_id(Object parent_category_id) {
        this.parent_category_id = parent_category_id;
        return this;
    }

    /**
     * Set the team_id
     *
     * @param team_id the value
     */
    public ClsCategorySub setTeam_id(Object team_id) {
        this.team_id = team_id;
        return this;
    }

    /**
     * Set the write_uid
     *
     * @param write_uid the value
     */
    public ClsCategorySub setWrite_uid(Object write_uid) {
        this.write_uid = write_uid;
        return this;
    }



}