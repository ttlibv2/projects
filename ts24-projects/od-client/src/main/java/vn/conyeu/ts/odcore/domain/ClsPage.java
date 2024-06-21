package vn.conyeu.ts.odcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.conyeu.commons.beans.ObjectMap;
import vn.conyeu.commons.utils.Objects;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class ClsPage extends ClsModel {
    Integer limit = 80;
    Integer offset = 0;
    Boolean sort;

    public ClsPage limit(int size) {
        setLimit(size);
        return this;
    }

    public ClsPage offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @JsonIgnore
    public int getPage() {
        return offset / limit;
    }

    @Override
    public ObjectMap cloneMap() {
        return super.cloneMap();
    }

    public void setDefaultLimit(int limit) {
        if(Objects.isLoe(this.limit, 0)) this.limit = limit;
    }
}