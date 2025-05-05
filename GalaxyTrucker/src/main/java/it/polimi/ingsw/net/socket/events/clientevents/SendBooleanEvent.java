package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class SendBooleanEvent extends Event {
    private boolean answer;
    public SendBooleanEvent(final boolean answer) {
        super(EventType.SEND_INFO_EVENT);
        this.answer = answer;
    }
    public boolean getAnswer() {
        return answer;
    }
}
