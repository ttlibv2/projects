package vn.conyeu.google.drives.builder;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import vn.conyeu.google.drives.GMime;
import vn.conyeu.google.query.BoolExpression;
import vn.conyeu.google.query.BoolOp;
import vn.conyeu.google.query.DslExpression;
import vn.conyeu.google.query.GOps;

import java.lang.reflect.AnnotatedElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

class QueryClass {
    public final Name name = new Name();
    public final MimeType mimeType = new MimeType();
    public final FullText fullText = new FullText();
    public final ModifiedTime modifiedTime = new ModifiedTime();
    public final ViewedByMeTime viewedByMeTime = new ViewedByMeTime();
    public final Trashed trashed = new Trashed();
    public final Starred starred = new Starred();
    public final Parents parents = new Parents();
    public final Owners owners = new Owners();
    public final Writters writters = new Writters();
    public final Readers readers = new Readers();
    public final SharedWithMe sharedWithMe = new SharedWithMe();
    public final CreatedTime createdTime = new CreatedTime();
    public final Properties properties = new Properties();
    public final AppProperties appProperties = new AppProperties();
    public final Visibility visibility = new Visibility();
    public final Hidden hidden = new Hidden();
    public final MemberCount memberCount = new MemberCount();
    public final OrganizerCount organizerCount = new OrganizerCount();


    public class Name extends StringPath {

        public Name() {
            super("name");
        }

        public BoolExpression like(String right) {
            return super.like(right);
        }

        public BoolExpression eq(String right) {
            return super.eq(right);
        }

        public BoolExpression neq(String right) {
            return super.neq(right);
        }

    }

    public class MimeType extends StringPath {

        public MimeType() {
            super("mimeType");
        }

        public BoolExpression like(String right) {
            return super.like(right);
        }

        public BoolExpression eq(GMime right) {
            return super.eq(right.getMime());
        }

        public BoolExpression eq(String right) {
            return super.eq(right);
        }

        public BoolExpression neq(GMime right) {
            return super.neq(right.getMime());
        }

        public BoolExpression neq(String right) {
            return super.neq(right);
        }
    }

    public class FullText extends StringPath {

        public FullText() {
            super("fullText");
        }

        public BoolExpression like(String right) {
            return super.like(right);
        }
    }

    public class ModifiedTime extends DateTimePath {

        public ModifiedTime() {
            super("modifiedTime");
        }
    }

    public class ViewedByMeTime extends DateTimePath {

        public ViewedByMeTime() {
            super("viewedByMeTime");
        }
    }

    public class Trashed extends BooleanPath {

        public Trashed() {
            super("trashed");
        }
    }

    public class Starred extends BooleanPath {

        public Starred() {
            super("starred");
        }
    }

    public class Parents extends InPath {

        public Parents() {
            super("parents");
        }
    }

    public class Owners extends InPath {

        public Owners() {
            super("owners");
        }
    }

    public class Writters extends InPath {

        public Writters() {
            super("writters");
        }
    }

    public class Readers extends InPath {

        public Readers() {
            super("readers");
        }
    }

    public class SharedWithMe extends BooleanPath {

        public SharedWithMe() {
            super("sharedWithMe");
        }
    }

    public class CreatedTime extends DateTimePath {

        public CreatedTime() {
            super("createdTime");
        }
    }

    public class Properties extends HasPath {

        public Properties() {
            super("properties");
        }
    }

    public class AppProperties extends HasPath {

        public AppProperties() {
            super("appProperties");
        }
    }

    public class Visibility extends BooleanPath {

        public Visibility() {
            super("visibility");
        }
    }

    public class Hidden extends BooleanPath {

        public Hidden() {
            super("hidden");
        }
    }

    public class MemberCount extends IntegerPath {

        public MemberCount() {
            super("memberCount");
        }
    }

    public class OrganizerCount extends IntegerPath {

        public OrganizerCount() {
            super("organizerCount");
        }
    }

    private class HasPath extends DrivePath<Map.Entry> {

        public HasPath(String variable) {
            super(Map.Entry.class, variable);
        }

        @Override
        public BoolExpression has(String key, String value) {
            return super.has(key, value);
        }

        @Override
        public BoolExpression hasKey(String key) {
            return super.hasKey(key);
        }

        @Override
        public BoolExpression hasValue(String value) {
            return super.hasValue(value);
        }
    }

    private class InPath extends DrivePath<String> {

        public InPath(String variable) {
            super(String.class, variable);
        }

        @Override
        public BoolExpression in(String right) {
            return super.in(right);
        }
    }

    private class IntegerPath extends DrivePath<Integer> {

        public IntegerPath(String variable) {
            super(Integer.class, variable);
        }

        public BoolExpression eq(Integer value) {
            return super.eq(value);
        }

        public BoolExpression neq(Integer value) {
            return super.neq(value);
        }

        public BoolExpression loe(Integer value) {
            return super.loe(value);
        }

        public BoolExpression lt(Integer value) {
            return super.lt(value);
        }

        public BoolExpression goe(Integer value) {
            return super.goe(value);
        }

        public BoolExpression gt(Integer value) {
            return super.gt(value);
        }

    }

    private class BooleanPath extends DrivePath<Boolean> {
        private transient volatile BoolExpression eqTrue, eqFalse;

        public BooleanPath(String variable) {
            super(Boolean.class, variable);
        }

        /**
         * Create a {@code this == true} expression
         * @return this == true
         */
        public BoolExpression isTrue() {
            if (eqTrue == null) {
                eqTrue = eq(true);
            }
            return eqTrue;
        }

        /**
         * Create a {@code this == false} expression
         * @return this == false
         */
        public BoolExpression isFalse() {
            if (eqFalse == null) {
                eqFalse = eq(false);
            }
            return eqFalse;
        }

    }

    private class DateTimePath extends DrivePath<String> {

        public DateTimePath(String variable) {
            super(String.class, variable);
        }

        public BoolExpression eq(String time) {
            return super.eq(time);
        }

        public BoolExpression neq(String time) {
            return super.neq(time);
        }

        public BoolExpression loe(String time) {
            return super.loe(time);
        }

        public BoolExpression lt(String time) {
            return super.lt(time);
        }

        public BoolExpression goe(String time) {
            return super.goe(time);
        }

        public BoolExpression gt(String time) {
            return super.gt(time);
        }

        public BoolExpression eq(LocalDateTime time) {
            return super.eq(format(time));
        }

        public BoolExpression neq(LocalDateTime time) {
            return super.neq(format(time));
        }

        public BoolExpression loe(LocalDateTime time) {
            return super.loe(format(time));
        }

        public BoolExpression lt(LocalDateTime time) {
            return super.lt(format(time));
        }

        public BoolExpression goe(LocalDateTime time) {
            return super.goe(format(time));
        }

        public BoolExpression gt(LocalDateTime time) {
            return super.gt(format(time));
        }

        protected String format(LocalDateTime dt) {
            return dt.format(DT_ISO);
        }
    }

    private class StringPath extends DrivePath<String> {
        public StringPath(String variable) {
            super(String.class, variable);
        }
    }

    public class DrivePath<T> extends DslExpression<T> implements Path<T> {
        private final PathImpl<T> pathMixin;

        public DrivePath(Class<? extends T> type, String variable) {
            this(type, PathMetadataFactory.forVariable(variable));
        }

        private DrivePath(Class<? extends T> type, PathMetadata metadata) {
            super(ExpressionUtils.path(type, metadata));
            this.pathMixin = (PathImpl<T>) mixin;
        }

        @Override
        public final AnnotatedElement getAnnotatedElement() {
            return pathMixin.getAnnotatedElement();
        }

        @Override
        public final Path<?> getRoot() {
            return pathMixin.getRoot();
        }

        @Override
        public final PathMetadata getMetadata() {
            return pathMixin.getMetadata();
        }

        @Override
        public final <R, C> R accept(Visitor<R, C> v, C context) {
            return v.visit(pathMixin, context);
        }

        public QueryClass and() {
            return QueryClass.this;
        }

        protected BoolExpression eq(T right) {
            Expression<T> constant = Expressions.constant(right);
            return new BoolOp(GOps.EQUAL, mixin, constant);
        }

        protected BoolExpression neq(T right) {
            Expression<T> constant = Expressions.constant(right);
            return new BoolOp(GOps.NOT_EQUAL, mixin, constant);
        }

        protected BoolExpression lt(T right) {
            Expression<T> constant = Expressions.constant(right);
            return new BoolOp(GOps.LESS_THAN, mixin, constant);
        }

        protected BoolExpression loe(T right) {
            Expression<T> constant = Expressions.constant(right);
            return new BoolOp(GOps.LESS_OR_EQUAL, mixin, constant);
        }

        protected BoolExpression gt(T right) {
            Expression<T> constant = Expressions.constant(right);
            return new BoolOp(GOps.GREATER_THAN, mixin, constant);
        }

        protected BoolExpression goe(T right) {
            Expression<T> constant = Expressions.constant(right);
            return new BoolOp(GOps.GREATER_OR_EQUAL, mixin, constant);
        }

        protected BoolExpression in(T right) {
            Expression<T> constant = Expressions.constant(right);
            return new BoolOp(DriveOps.IN, mixin, constant);
        }

        protected BoolExpression like(T right) {
            Expression<T> constant = Expressions.constant(right);
            return new BoolOp(GOps.LIKE, mixin, constant);
        }

        protected BoolExpression has(String key, String value) {
            Expression<String> keyEx = Expressions.constant(key);
            Expression<String> valueEx = Expressions.constant(value);
            return new BoolOp(DriveOps.HAS, mixin, keyEx, valueEx);
        }

        protected BoolExpression hasKey(String key) {
            Expression<String> keyEx = Expressions.constant(key);
            return new BoolOp(DriveOps.HAS_KEY, mixin, keyEx);
        }

        protected BoolExpression hasValue(String value) {
            Expression<String> valueEx = Expressions.constant(value);
            return new BoolOp(DriveOps.HAS_VALUE, mixin, valueEx);
        }

    }

    public enum DriveOps implements Operator {
        IN(Boolean.class),
        HAS(Boolean.class),
        HAS_KEY(Boolean.class),
        HAS_VALUE(Boolean.class);

        final Class type;

        DriveOps(Class type) {
            this.type = type;
        }

        @Override
        public Class getType() {
            return type;
        }
    }


    private static final DateTimeFormatter DT_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

}