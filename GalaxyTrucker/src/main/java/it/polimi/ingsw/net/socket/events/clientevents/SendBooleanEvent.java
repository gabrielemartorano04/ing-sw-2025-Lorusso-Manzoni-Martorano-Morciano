package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

public class SendBooleanEvent extends ClientEvent {
    private boolean answer;
    public SendBooleanEvent(final boolean answer) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.answer = answer;
    }
    public boolean getAnswer() {
        return answer;
    }
}
