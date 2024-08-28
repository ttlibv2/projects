package vn.conyeu.demo.querydsl;

import vn.conyeu.commons.utils.Objects;
import java.io.Serializable;

public class PathMetadata implements Serializable {
    private final Object element;
    private final int hashCode;
    private final Path parent, rootPath;
    private final PathType pathType;

    public PathMetadata(Path parent, Object element, PathType type) {
        this.parent = parent;
        this.element = element;
        this.pathType = type;
        this.rootPath = parent != null ? parent.getRoot() : null;
        this.hashCode = 31 * element.hashCode() + pathType.name().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;
        else if(!(obj instanceof PathMetadata p)) return false;
        else return element.equals(p.element) &&
                    pathType == p.pathType &&
                    Objects.equals(parent, p.parent);
    }

    public Object getElement() {
        return element;
    }

    public String getName() {
        if (pathType == PathType.VARIABLE || pathType == PathType.PROPERTY) {
            return (String) element;
        } else {
            throw new IllegalStateException("name property not available for path of type " + pathType +
                    ". Use getElement() to access the generic path element.");
        }
    }

    public Path getParent() {
        return parent;
    }

    public PathType getPathType() {
        return pathType;
    }

    public Path getRootPath() {
        return rootPath;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public boolean isRoot() {
        return parent == null || (pathType == PathType.DELEGATE && parent.getMetadata().isRoot());
    }

    /**
     * Create a new PathMetadata instance for a variable
     *
     * @param variable variable name
     * @return variable path
     */
    public static PathMetadata forVariable(String variable) {
        return new PathMetadata(null, variable, PathType.VARIABLE);
    }

    /**
     * Create a new PathMetadata instance for property access
     *
     * @param parent parent path
     * @param property property name
     * @return property path
     */
    public static PathMetadata forProperty(Path<?> parent, String property) {
        return new PathMetadata(parent, property, PathType.PROPERTY);
    }
}