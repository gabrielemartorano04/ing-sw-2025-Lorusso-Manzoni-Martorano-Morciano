package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class DiscardSuccessEvent extends ServerEvent {

    private String message;
    private int index;
    private String nickname;
    public DiscardSuccessEvent(int index, String nickname) {
        super(ServerEventType.DISCART_SUCCESS, true);
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
