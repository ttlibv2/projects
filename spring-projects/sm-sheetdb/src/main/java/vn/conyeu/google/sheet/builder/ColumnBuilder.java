package vn.conyeu.google.sheet.builder;

import lombok.Getter;
import vn.conyeu.google.xsldb.ColumnType;

@Getter
public class ColumnBuilder {
    private String name;
    private ColumnType type;
    private Integer index;

    /**
     * Set the name
     *
     * @param name the value
     */
    public ColumnBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the type
     *
     * @param type the value
     */
    public ColumnBuilder type(ColumnType type) {
        this.type = type;
        return this;
    }

    /**
     * Set the index
     *
     * @param index the value
     */
    public ColumnBuilder index(Integer index) {
        this.index = index;
        return this;
    }
}