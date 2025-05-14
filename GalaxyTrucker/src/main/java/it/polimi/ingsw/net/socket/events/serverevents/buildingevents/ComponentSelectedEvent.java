package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

/**
 * Event from server to client that returns the selected successfully component
 */
public class ComponentSelectedEvent extends ServerEvent {
    private String message;
    private int index;
    private String nickname;

    public ComponentSelectedEvent(int index, String nickname) {
        super(ServerEventType.COMPONENT_SELECTED, true);
        this.index=index;
        this.nickname=nickname;
        message="Component selected";
    }
    public int getIndex() {
        return index;
    }

    public String getNickname() {
        return nickname;
    }


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
