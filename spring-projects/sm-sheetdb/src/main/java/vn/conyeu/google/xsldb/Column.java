package vn.conyeu.google.xsldb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.conyeu.google.sheet.builder.SheetUtil;

@Getter
@ToString
@NoArgsConstructor
public class Column {
    private String columnId;
    private String columnName;
    private ColumnType columnType = ColumnType.STRING;
    private Integer length;
    private Integer decimal;
    private String comment;
    private Increment increment;
    private String tableId;
    private Boolean nullable;
    private Boolean primaryKey;
    private Boolean unique;
    private Boolean insertable;
    private Boolean updatable;
    private String valueDefault;
    private Integer position;
    private String letter;

    public Column(String columnName, ColumnType columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public Column position(Integer position) {
        this.position = position;
        this.letter = position == null ? null : SheetUtil.numberToLetter(position);
        return this;
    }

    /**
     * Set the columnId
     *
     * @param columnId the value
     */
    public Column columnId(String columnId) {
        this.columnId = columnId;
        return this;
    }

    /**
     * Set the columnName
     *
     * @param columnName the value
     */
    public Column columnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    /**
     * Set the columnType
     *
     * @param columnType the value
     */
    public Column columnType(ColumnType columnType) {
        this.columnType = columnType;
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
     * Set the tableId
     *
     * @param tableId the value
     */
    public Column tableId(String tableId) {
        this.tableId = tableId;
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
    public Column primaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
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
    public Column valueDefault(String valueDefault) {
        this.valueDefault = valueDefault;
        return this;
    }
}