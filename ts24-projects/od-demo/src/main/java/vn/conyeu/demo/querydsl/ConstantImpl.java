package vn.conyeu.demo.querydsl;

public final class ConstantImpl<T> extends ExpressionBase<T> implements Constant<T> {
    private static final int CACHE_SIZE = 256;
    private final T constant;

    private ConstantImpl(T constant) {
        this((Class) constant.getClass(), constant);
    }

    public ConstantImpl(Class<T> type, T constant) {
        super(type);
        this.constant = constant;
    }

    public T getConstant() {
        return constant;
    }

    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        else if (!(o instanceof Constant c)) return false;
        else return c.getConstant().equals(constant);
    }

    public static Constant<Boolean> create(boolean b) {
        return b ? Constants.TRUE : Constants.FALSE;
    }

    public static <T> Constant<T> create(T obj) {
        return new ConstantImpl<T>(obj);
    }

    public static <T> Constant<T> create(Class<T> type, T constant) {
        return new ConstantImpl<T>(type, constant);
    }

    private static class Constants {
        private static final Constant<Boolean> FALSE = new ConstantImpl<>(Boolean.FALSE);
        private static final Constant<Boolean> TRUE = new ConstantImpl<>(Boolean.TRUE);
    }

}