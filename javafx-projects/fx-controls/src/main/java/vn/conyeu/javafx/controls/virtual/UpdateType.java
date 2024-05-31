package vn.conyeu.javafx.controls.virtual;


/**
 * Enumeration to differentiate the various types of {@link FlowState}.
 * <p>
 * To be more precise, this is used to indicate which event lead to the creation of the new state.
 */
public enum UpdateType {

    /**
     * Indicates that the state has been created after an initialization request
     */
    INIT,

    /**
     * Indicates that the state has been created after a scroll request
     */
    SCROLL,

    /**
     * Indicates that the state has been created after a change in the items list
     */
    CHANGE
}