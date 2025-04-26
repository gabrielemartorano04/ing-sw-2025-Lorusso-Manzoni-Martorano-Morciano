package it.polimi.ingsw.net.events;

public class HelloEvent extends Event {
    private final String message;
    private final EventType eventType;

    public HelloEvent(String message, EventType eventType) {
        this.message = message;
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "TextEvent{ message='" + message + "' }";
    }
}