package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.events.EventType;

public class BooleanRequest extends ServerEvent {
    private String question;
    private int userId;

    public BooleanRequest(EventType eventType, String question, int Id) {
        super(ServerEventType.REQUEST);
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
