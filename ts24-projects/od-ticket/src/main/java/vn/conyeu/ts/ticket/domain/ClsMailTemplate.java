package vn.conyeu.ts.ticket.domain;

import lombok.EqualsAndHashCode;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.ts.odcore.domain.ClsModel;

@EqualsAndHashCode(callSuper = false)
public class ClsMailTemplate extends ClsModel<ClsMailTemplate> {
   private Long id;

    public static ClsMailTemplate from(ObjectMap obj) {
        return obj.asObject(ClsMailTemplate.class);
    }

    /**
     * Returns the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id the value
     */
    public ClsMailTemplate setId(Long id) {
        this.id = id;
        return this;
    }
}