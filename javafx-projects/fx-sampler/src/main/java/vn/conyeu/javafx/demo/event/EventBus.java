package vn.conyeu.javafx.demo.event;


import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public interface EventBus {

    static EventBus getDefault() {
        return DefaultEventBus.getInstance();
    }

    /**
     * Subscribe to an event type.
     *
     * @param eventType  the event type, can be a super class of all events to subscribe.
     * @param subscriber the subscriber which will consume the events.
     * @param <T>        the event type class.
     */
    <T extends Event> void subscribe(Class<? extends T> eventType, Consumer<T> subscriber);

    /**
     * Unsubscribe from all event types.
     *
     * @param subscriber the subscriber to unsubscribe.
     */
    <T extends Event> void unsubscribe(Consumer<T> subscriber);

    /**
     * Unsubscribe from an event type.
     *
     * @param eventType  the event type, can be a super class of all events to unsubscribe.
     * @param subscriber the subscriber to unsubscribe.
     * @param <T>        the event type class.
     */
    <T extends Event> void unsubscribe(Class<? extends T> eventType, Consumer<T> subscriber);

    /**
     * Publish an event to all subscribers.
     *
     * <p>The event type is the class of <code>event</code>. The event is published to all consumers which subscribed to
     * this event type or any super class.
     *
     * @param event the event.
     */
    <T extends Event> void publish(T event);

    /**
     * Simple event bus implementation.
     *
     * <p>Subscribe and publish events. Events are published in channels distinguished by event type.
     * Channels can be grouped using an event type hierarchy.
     *
     * <p>You can use the default event bus instance {@link #getInstance}, which is a singleton,
     * or you can create one or multiple instances of {@link DefaultEventBus}.
     */
    final class DefaultEventBus implements EventBus {

        private DefaultEventBus() {
        }

        private final Map<Class<?>, Set<Consumer>> subscribers = new ConcurrentHashMap<>();

        @Override
        public <E extends Event> void subscribe(Class<? extends E> eventType, Consumer<E> subscriber) {
            Objects.requireNonNull(eventType);
            Objects.requireNonNull(subscriber);

            Set<Consumer> eventSubscribers = getOrCreateSubscribers(eventType);
            eventSubscribers.add(subscriber);
        }

        private <E> Set<Consumer> getOrCreateSubscribers(Class<E> eventType) {
            Set<Consumer> eventSubscribers = subscribers.get(eventType);
            if (eventSubscribers == null) {
                eventSubscribers = new CopyOnWriteArraySet<>();
                subscribers.put(eventType, eventSubscribers);
            }
            return eventSubscribers;
        }

        @Override
        public <E extends Event> void unsubscribe(Consumer<E> subscriber) {
            Objects.requireNonNull(subscriber);
            subscribers.values().forEach(eventSubscribers -> eventSubscribers.remove(subscriber));
        }

        @Override
        public <E extends Event> void unsubscribe(Class<? extends E> eventType, Consumer<E> subscriber) {
            Objects.requireNonNull(eventType);
            Objects.requireNonNull(subscriber);

            subscribers.keySet().stream()
                    .filter(eventType::isAssignableFrom)
                    .map(subscribers::get)
                    .forEach(eventSubscribers -> eventSubscribers.remove(subscriber));
        }

        @Override
        public <E extends Event> void publish(E event) {
            Objects.requireNonNull(event);

            Class<?> eventType = event.getClass();
            subscribers.keySet().stream()
                    .filter(type -> type.isAssignableFrom(eventType))
                    .flatMap(type -> subscribers.get(type).stream())
                    .forEach(subscriber -> publish(event, subscriber));
        }

        private <E extends Event> void publish(E event, Consumer<E> subscriber) {
            try {subscriber.accept(event);}
            catch (Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
            }
        }

        private static class InstanceHolder {
            private static final DefaultEventBus INSTANCE = new DefaultEventBus();
        }

        public static DefaultEventBus getInstance() {
            return InstanceHolder.INSTANCE;
        }
    }
}