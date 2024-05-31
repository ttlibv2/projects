package vn.conyeu.javafx.bindings;

import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class BindingBuilder<T, B extends Binding<? super T>> {

    public static BooleanBindingBuilder booleanBuilder() {
        return BooleanBindingBuilder.build();
    }

    public static StringBindingBuilder stringBuilder() {
        return StringBindingBuilder.build();
    }

    public static DoubleBindingBuilder doubleBuilder() {
        return DoubleBindingBuilder.build();
    }

    public static <T> ObjectBindingBuilder<T> objectBuilder() {
        return ObjectBindingBuilder.build();
    }

    public static FloatBindingBuilder floatBuilder() {
        return FloatBindingBuilder.build();
    }

    public static IntegerBindingBuilder integerBuilder() {
        return IntegerBindingBuilder.build();
    }

    public static LongBindingBuilder longBuilder() {
        return LongBindingBuilder.build();
    }

    //========================================================

    protected ObservableList<Observable> sources = FXCollections.observableArrayList();
    protected Callable<T> mapper;

    protected abstract B create();

    public final BindingBuilder<T, B> addSources(Observable... sources) {
        this.sources.addAll(sources);
        return this;
    }

    public final BindingBuilder<T, B> setSources(Observable... sources) {
       this.sources.setAll(sources);
        return this;
    }

    public final BindingBuilder<T, B> setMapper(Callable<T> mapper) {
        this.mapper = mapper;
        return this;
    }

    public final List<Observable> getSources() {
        return sources;
    }

    public final Observable[] getSourcesArray() {
        return sources.toArray(Observable[]::new);
    }

    public final Callable<T> getMapper() {
        return mapper;
    }

    public final B get() {
        return create();
    }
}