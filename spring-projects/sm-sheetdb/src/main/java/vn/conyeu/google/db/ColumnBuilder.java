package vn.conyeu.google.db;

import vn.conyeu.google.sheet.builder.XmlBuilder;

public class ColumnBuilder implements XmlBuilder<Column> {
    private final Column column = new Column();

    @Override
    public Column build() {
        return column;
    }

    public String getColumnName() {
        return column.getColumnName();
    }

    /**
     * Set the columnId
     *
     * @param columnId the value
     */
    public ColumnBuilder columnId(String columnId) {
        column.columnId(columnId);
        return this;
    }

    /**
     * Set the columnName
     *
     * @param columnName the value
     */
    public ColumnBuilder columnName(String columnName) {
        column.columnName(columnName);
        return this;
    }

    /**
     * Set the columnType
     *
     * @param columnType the value
     */
    public ColumnBuilder columnType(ColumnType columnType) {
        column.columnType(columnType);
        return this;
    }

    /**
     * Set the length
     *
     * @param length the value
     */
    public ColumnBuilder length(Integer length) {
        column.length(length);
        return this;
    }

    /**
     * Set the decimal
     *
     * @param decimal the value
     */
    public ColumnBuilder decimal(Integer decimal) {
        column.decimal(decimal);
        return this;
    }

    /**
     * Set the comment
     *
     * @param comment the value
     */
    public ColumnBuilder comment(String comment) {
        column.comment(comment);
        return this;
    }

    /**
     * Set the increment
     *
     * @param increment the value
     */
    public ColumnBuilder increment(Increment increment) {
        column.increment(increment);
        return this;
    }

    /**
     * Set the tableId
     *
     * @param tableId the value
     */
    public ColumnBuilder tableId(String tableId) {
        column.tableId(tableId);
        return this;
    }

    /**
     * Set the nullable
     *
     * @param nullable the value
     */
    public ColumnBuilder nullable(Boolean nullable) {
        column.nullable(nullable);
        return this;
    }

    /**
     * Set the primaryKey
     *
     * @param primaryKey the value
     */
    public ColumnBuilder primaryKey(Boolean primaryKey) {
        column.primaryKey(primaryKey);
        return this;
    }

    /**
     * Set the unique
     *
     * @param unique the value
     */
    public ColumnBuilder unique(Boolean unique) {
        column.unique(unique);
        return this;
    }

    /**
     * Set the insertable
     *
     * @param insertable the value
     */
    public ColumnBuilder insertable(Boolean insertable) {
        column.insertable(insertable);
        return this;
    }

    /**
     * Set the updatable
     *
     * @param updatable the value
     */
    public ColumnBuilder updatable(Boolean updatable) {
        column.updatable(updatable);
        return this;
    }

    /**
     * Set the valueDefault
     *
     * @param valueDefault the value
     */
    public ColumnBuilder valueDefault(String valueDefault) {
        column.valueDefault(valueDefault);
        return this;
    }
}