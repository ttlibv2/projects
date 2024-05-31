package vn.conyeu.javafx.glisten;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class LifecycleEvent extends Event {

    public static final EventType<LifecycleEvent>   ANY = new EventType(Event.ANY, "LIFECYCLE_EVENT");
    public static final EventType<LifecycleEvent>   SHOWING = new EventType(ANY, "SHOWING");
    public static final EventType<LifecycleEvent>  SHOWN = new EventType(ANY, "SHOWN");
    public static final EventType<LifecycleEvent>  HIDING = new EventType(ANY, "HIDING");
    public static final EventType<LifecycleEvent>  HIDDEN = new EventType(ANY, "HIDDEN");
    public static final EventType<LifecycleEvent>  CLOSE_REQUEST = new EventType(ANY, "CLOSE_REQUEST");

    public LifecycleEvent(EventTarget sourceAndTarget, EventType<? extends Event> eventType) {
        super(sourceAndTarget, sourceAndTarget, eventType);
    }


}