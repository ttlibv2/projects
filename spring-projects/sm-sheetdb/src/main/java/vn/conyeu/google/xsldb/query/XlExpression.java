package vn.conyeu.google.xsldb.query;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.DslExpression;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;

public abstract class XlExpression<T> implements Expression<T>  {
    protected final Expression<T> mixin;
    protected final int hashCode;

    public XlExpression(Expression<T> mixin) {
        this.mixin = mixin;
        this.hashCode = mixin.hashCode();
    }

    public final Class<? extends T> getType() {
        return mixin.getType();
    }

    /**
     * Create an alias for the expression
     * @return this as alias
     */
    public XlExpression<T> as(String alias) {
        Expression aliasPath = ExpressionUtils.path(getType(), alias);
        return new XlOperation(getType(), XlOps.ALIAS, mixin, aliasPath);
    }

    public boolean equals(Object o) { // can be overwritten
        return mixin.equals(o);
    }

    public final int hashCode() {
        return hashCode;
    }

    public final String toString() {
        return mixin.toString();
    }

    public static class XlOperation<T> extends XlExpression<T> implements Operation<T> {
        private final OperationImpl<T> opMixin;

        public XlOperation(Class<? extends T> type, Operator op, Expression<?>... args) {
            this(type, op, Arrays.asList(args));
        }

        public XlOperation(Class<? extends T> type, Operator op, List<Expression<?>> args) {
            super(ExpressionUtils.operation(type, op, args));
            this.opMixin = (OperationImpl<T>) mixin;
        }

        @Override
        public Expression<?> getArg(int index) {
            return opMixin.getArg(index);
        }

        @Override
        public List<Expression<?>> getArgs() {
            return opMixin.getArgs();
        }

        @Override
        public Operator getOperator() {
            return opMixin.getOperator();
        }

        @Override
        public Object accept(Visitor v, Object context) {
            return v.visit(this, context);
        }
    }

    public static class XlPath<T> extends XlExpression<T> implements Path<T> {
        private final PathImpl<T> pathMixin;

        public XlPath(Class<? extends T> type, Path<?> parent, String property) {
            this(type, PathMetadataFactory.forProperty(parent, property));
        }

        public XlPath(Class<? extends T> type, PathMetadata metadata) {
            super(ExpressionUtils.path(type, metadata));
            this.pathMixin = (PathImpl<T>) mixin;
        }

        protected XlPath(Class<? extends T> type, String var) {
            this(type, PathMetadataFactory.forVariable(var));
        }

        public final <R,C> R accept(Visitor<R,C> v, C context) {
            return v.visit(pathMixin, context);
        }

        public PathMetadata getMetadata() {
            return pathMixin.getMetadata();
        }

        public Path<?> getRoot() {
            return pathMixin.getRoot();
        }

        public AnnotatedElement getAnnotatedElement() {
            return pathMixin.getAnnotatedElement();
        }
    }

    public static class XlTemplate<T> extends XlExpression<T> implements TemplateExpression<T> {

        private final TemplateExpressionImpl<T> templateMixin;

        public XlTemplate(Class<? extends T> type, Template template, List<?> args) {
            super(ExpressionUtils.template(type, template, args));
            templateMixin = (TemplateExpressionImpl<T>) mixin;
        }

        public final <R,C> R accept(Visitor<R,C> v, C context) {
            return v.visit(templateMixin, context);
        }

        public Object getArg(int index) {
            return templateMixin.getArg(index);
        }

        public List<?> getArgs() {
            return templateMixin.getArgs();
        }

        public Template getTemplate() {
            return templateMixin.getTemplate();
        }
    }




}