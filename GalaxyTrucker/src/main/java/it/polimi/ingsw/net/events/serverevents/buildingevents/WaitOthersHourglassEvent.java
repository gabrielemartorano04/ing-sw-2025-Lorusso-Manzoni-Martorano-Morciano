package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class WaitOthersHourglassEvent extends ServerEvent {
    private final String nickname;
    public WaitOthersHourglassEvent(String nickname) {
        super(true);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
