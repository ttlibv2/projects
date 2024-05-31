package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.ts.odcore.domain.ClsModel;

@Getter @EqualsAndHashCode(callSuper = false)
public class ClsCategory extends ClsModel<ClsCategory> {
    private Long id;
    private Long sequence;
    private String name;
    private String display_name;

    public static ClsCategory from(Object category_id) {
        if (category_id == null) return null;
        else return from(ClsModel.objectToMap(category_id));
    }

    public static ClsCategory from(ObjectMap obj) {
        return obj.asObject(ClsCategory.class);
    }

    /**
     * Set the id
     *
     * @param id the value
     */
    public ClsCategory setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Set the sequence
     *
     * @param sequence the value
     */
    public ClsCategory setSequence(Long sequence) {
        this.sequence = sequence;
        return this;
    }

    /**
     * Set the name
     *
     * @param name the value
     */
    public ClsCategory setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the display_name
     *
     * @param display_name the value
     */
    public ClsCategory setDisplay_name(String display_name) {
        this.display_name = display_name;
        return this;
    }

    public String getDisplay_name() {
        if (Objects.isBlank(display_name)) display_name = name;
        return display_name;
    }

    @Override
    public String toString() {
        return getDisplay_name();
    }


}