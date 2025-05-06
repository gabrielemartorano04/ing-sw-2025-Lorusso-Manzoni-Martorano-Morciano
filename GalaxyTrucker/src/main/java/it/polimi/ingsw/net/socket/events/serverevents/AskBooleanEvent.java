package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class AskBooleanEvent extends Event {
    private String question;
    private int userId;

    public AskBooleanEvent(EventType eventType, String question, int Id) {
        super(eventType);
        this.question = question;
        this.userId = Id;
    }

    public String getQuestion() {
        return question;
    }

    public int getUserId() {
        return userId;
    }
}
