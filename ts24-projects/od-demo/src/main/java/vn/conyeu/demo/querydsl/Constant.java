package vn.conyeu.demo.querydsl;

public interface Constant<T> extends Expression<T> {
  T getConstant();
}