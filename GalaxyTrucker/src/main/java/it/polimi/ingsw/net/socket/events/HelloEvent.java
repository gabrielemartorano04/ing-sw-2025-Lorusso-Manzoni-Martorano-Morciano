package it.polimi.ingsw.net.socket.events;


public class HelloEvent extends Event {
    private final String message;

    public HelloEvent(String message, EventType eventType) {
        super(eventType);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "HelloEvent{ message='" + message + "' }";
    }
}
