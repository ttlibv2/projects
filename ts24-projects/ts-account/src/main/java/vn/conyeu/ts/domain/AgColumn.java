package vn.conyeu.ts.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import vn.conyeu.commons.beans.ObjectMap;

import java.io.Serializable;

public class AgColumn implements Serializable {
    private String field;
    private String headerName;
    private Object columnType;
    private Double width;
    private Boolean hide;
    private Boolean editable;
    private Boolean sort;
    private Object pinned;
    private Boolean resizable;
    private Boolean checkboxSelection;
    private Boolean headerCheckboxSelection;
    private Boolean pivot;

    private String cellDataType;

    private String valueGetter;
    private Integer position;

    @JsonAnySetter
    private ObjectMap custom;

    @JsonAnyGetter
    public ObjectMap getCustom() {
        custom = ObjectMap.ifNull(custom);
        return custom;
    }
}