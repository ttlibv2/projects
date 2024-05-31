package vn.conyeu.javafx.demo.event;

import java.util.UUID;

public abstract class Event {

    public static PageEvent pageEvent(PageEvent.Action action) {
        return new PageEvent(action);
    }

    public static ThemeEvent themeEvent(ThemeEvent.EventType eventType) {
        return new ThemeEvent(eventType);
    }

    protected final UUID id = UUID.randomUUID();

    protected Event() {
    }

    public UUID getId() {
        return id;
    }

    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        else return obj instanceof Event evt && evt.id.equals(id);
    }

    public static <E extends Event> void publish(E event) {
        EventBus.getDefault().publish(event);
    }

}