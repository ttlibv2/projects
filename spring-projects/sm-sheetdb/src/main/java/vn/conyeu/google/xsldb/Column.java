package vn.conyeu.google.xsldb;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vn.conyeu.commons.utils.Jsons;
import vn.conyeu.google.sheet.builder.SheetUtil;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Column {
    private String name;
    private ColumnType type = ColumnType.STRING;
    private String pattern;
    private Integer length;
    private Integer decimal;
    private String comment;
    private Increment increment;
    private Boolean nullable;
    private Boolean key;
    private Boolean unique;
    private Boolean insertable;
    private Boolean updatable;
    private String value;
    private Integer index;
    private String letter;

    public Column(String name, ColumnType type) {
        this.name = name;
        this.type = type;
    }

    public Column index(Integer position) {
        this.index = position;
        this.letter = position == null ? null : SheetUtil.numberToLetter(position);
        return this;
    }

    /**
     * Set the columnName
     *
     * @param columnName the value
     */
    public Column name(String columnName) {
        this.name = columnName;
        return this;
    }

    /**
     * Set the columnType
     *
     * @param columnType the value
     */
    public Column type(ColumnType columnType) {
        this.type = columnType;
        return this;
    }

    /**
     * Set the pattern
     *
     * @param pattern the value
     */
    public Column pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * Set the length
     *
     * @param length the value
     */
    public Column length(Integer length) {
        this.length = length;
        return this;
    }

    /**
     * Set the decimal
     *
     * @param decimal the value
     */
    public Column decimal(Integer decimal) {
        this.decimal = decimal;
        return this;
    }

    /**
     * Set the comment
     *
     * @param comment the value
     */
    public Column comment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Set the increment
     *
     * @param increment the value
     */
    public Column increment(Increment increment) {
        this.increment = increment;
        return this;
    }

    /**
     * Set the nullable
     *
     * @param nullable the value
     */
    public Column nullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    /**
     * Set the primaryKey
     *
     * @param primaryKey the value
     */
    public Column key(Boolean primaryKey) {
        this.key = primaryKey;
        return this;
    }

    /**
     * Set the unique
     *
     * @param unique the value
     */
    public Column unique(Boolean unique) {
        this.unique = unique;
        return this;
    }

    /**
     * Set the insertable
     *
     * @param insertable the value
     */
    public Column insertable(Boolean insertable) {
        this.insertable = insertable;
        return this;
    }

    /**
     * Set the updatable
     *
     * @param updatable the value
     */
    public Column updatable(Boolean updatable) {
        this.updatable = updatable;
        return this;
    }

    /**
     * Set the valueDefault
     *
     * @param valueDefault the value
     */
    public Column value(String valueDefault) {
        this.value = valueDefault;
        return this;
    }

    /**
     * Returns the pattern
     */
    public String getPattern() {
        return pattern == null && type != null ? type.pattern : pattern;
    }

    public String toString() {
       return Jsons.serializeToString(this, false);
    }
}