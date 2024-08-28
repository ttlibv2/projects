package vn.conyeu.demo.querydsl;

/**
 * {@code PathType} represents the relation of a {@link Path} to its parent
 */
public enum PathType implements Operator {
    /**
     * Indexed array access (array[i])
     */
    ARRAYVALUE,

    /**
     * Indexed array access with constant (array[i])
     */
    ARRAYVALUE_CONSTANT,

    /**
     * Access of any element in a collection
     */
    COLLECTION_ANY,

    /**
     * Delegate to an expression
     */
    DELEGATE,

    /**
     * Indexed list access (list.get(index))
     */
    LISTVALUE,

    /**
     * Indexed list access with constant (list.get(index))
     */
    LISTVALUE_CONSTANT,

    /**
     * Map value access (map.get(key))
     */
    MAPVALUE,

    /**
     * Map value access with constant (map.get(key))
     */
    MAPVALUE_CONSTANT,

    /**
     * Property of the parent
     */
    PROPERTY,

    /**
     * Root path
     */
    VARIABLE,

    /**
     * Treated path
     */
    TREATED_PATH;

    @Override
    public Class<?> getType() {
        return Object.class;
    }

}