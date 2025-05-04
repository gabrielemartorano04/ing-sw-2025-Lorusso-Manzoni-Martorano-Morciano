package it.polimi.ingsw.net.socket.events;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;

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
