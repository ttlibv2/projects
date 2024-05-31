package vn.conyeu.javafx.property.sync;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.function.Supplier;

public final class ExecutionUtils {

    /**
     * Executes the given truAction if the given expression and the executeTrueNow parameter are true.
     * <p>
     * Executes the given falseAction if the given expression is false and the executeFalseNow parameter is true.
     * <p>
     * If the given expression is false or the addListenerIfTrue parameter is true, adds a listener to
     * the expression and executes the given trueAction every time the property becomes true, and the given
     * falseAction every time the property becomes false, or just once if the isOneShot parameter is true.
     *
     * @param booleanExpression the expression to evaluate
     * @param trueAction        the action to execute when the expression is true
     * @param falseAction       the action to execute when the expression is false
     * @param executeTrueNow    to specify if the given trueAction should be immediately executed if the expression is already true
     * @param executeFalseNow   to specify if the given falseAction should be immediately executed if the expression is already false
     * @param addListenerIfTrue to specify if the listener should be added anyway to the expression even if it is already true
     * @param isOneShot         to specify if the added listener should be removed after the first time the expression becomes true
     */
    public static void executeWhen(BooleanExpression booleanExpression, Runnable trueAction, Runnable falseAction,
                                   boolean executeTrueNow, boolean executeFalseNow, boolean addListenerIfTrue, boolean isOneShot) {

        final boolean hasBool = booleanExpression.get();
        if(hasBool && executeTrueNow) trueAction.run();
        else if(!hasBool && executeFalseNow) falseAction.run();

        if (!hasBool || addListenerIfTrue) {
            booleanExpression.addListener(new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) trueAction.run();
                    else falseAction.run();
                    if (isOneShot) booleanExpression.removeListener(this);
                }
            });
        }
    }

    /**
     * Executes the given action when the given {@link Observable} changes.
     * <p>
     * If executeNow is true the action is immediately executed.
     * <p>
     * Adds a listener to the observable and executes the given action every time the observable changes and the
     * execution condition is met or just once if the isOneShot parameter is true.
     *
     * @param observable         the observable to listen to
     * @param action             the action to execute when the observable changes
     * @param executeNow         to specify if the given action should be immediately executed
     * @param executionCondition to specify on what conditions the action should be executed
     * @param isOneShot          to specify if the added listener should be removed after the first time the observable changes
     */
    public static void executeWhen(Observable observable, Runnable action, boolean executeNow,
                                   Supplier<Boolean> executionCondition, boolean isOneShot) {
        if (executeNow) {
            action.run();
        }

        observable.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (executionCondition.get()) {
                    action.run();
                    if (isOneShot) {
                        observable.removeListener(this);
                    }
                }
            }
        });
    }




}