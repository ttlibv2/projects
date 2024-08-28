package vn.conyeu.demo.querydsl;

import java.lang.reflect.AnnotatedElement;

public class PathImpl<T> extends ExpressionBase<T> implements Path<T> {
    private final PathMetadata metadata;
    private final Path root;

    private transient AnnotatedElement annotatedElement;

    public PathImpl(Class<? extends T> type, String variable) {
        this(type, PathMetadata.forVariable(variable));
    }

    public PathImpl(Class<? extends T> type, PathMetadata metadata) {
        super(type);
        this.metadata = metadata;
        this.root = metadata.getRootPath() != null ? metadata.getRootPath() : this;
    }

    public PathImpl(Class<? extends T> type, Path parent, String property) {
        this(type, PathMetadata.forProperty(parent, property));
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) return true;
        else if(!(o instanceof Path p)) return false;
        else return p.getMetadata().equals(metadata);
    }

    @Override
    public final PathMetadata getMetadata() {
        return metadata;
    }

    @Override
    public final Path<?> getRoot() {
        return root;
    }

    @Override
    public final AnnotatedElement getAnnotatedElement() {
        if (annotatedElement == null) {
            if (metadata.getPathType() == PathType.PROPERTY) {
                Class<?> beanClass = metadata.getParent().getType();
                String propertyName = metadata.getName();
                annotatedElement = Utils.getAnnotatedElement(beanClass, propertyName, getType());

            } else {
                annotatedElement = getType();
            }
        }
        return annotatedElement;
    }

    @Override
    public final <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

}