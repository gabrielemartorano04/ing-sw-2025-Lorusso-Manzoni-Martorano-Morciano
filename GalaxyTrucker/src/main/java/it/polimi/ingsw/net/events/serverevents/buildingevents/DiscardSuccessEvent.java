package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class DiscardSuccessEvent extends ServerEvent {

    private String message;
    private int index;
    private String nickname;
    public DiscardSuccessEvent(int index, String nickname) {
        super(true);
        message = "Discard success";
        this.index = index;
        this.nickname = nickname;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getNickname() {
        return nickname;
    }
}
