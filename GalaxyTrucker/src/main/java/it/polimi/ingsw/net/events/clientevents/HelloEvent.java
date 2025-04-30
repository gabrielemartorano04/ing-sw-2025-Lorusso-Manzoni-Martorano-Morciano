package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.events.Event;

public class HelloEvent extends Event {
    private final String message;

    public HelloEvent(String message, ClientEventType eventType) {
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
