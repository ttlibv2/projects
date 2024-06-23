package vn.conyeu.common.converter;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public abstract class SetToString<E> extends CollectToString<E, Set<E>> {

    public SetToString(Class<E> objectCls) {
        super(objectCls);
    }

    public SetToString(Class<E> objectCls, String delimiter) {
        super(objectCls, delimiter);
    }

    public SetToString(Class<E> objectCls, Set<String> fields) {
        super(objectCls, fields);
    }

    public SetToString(Class<E> objectCls, String delimiter, Set<String> fields) {
        super(objectCls, delimiter, fields);
    }

    @Override
    protected final Supplier<Set<E>> supplierNew() {
        return HashSet::new;
    }

}
