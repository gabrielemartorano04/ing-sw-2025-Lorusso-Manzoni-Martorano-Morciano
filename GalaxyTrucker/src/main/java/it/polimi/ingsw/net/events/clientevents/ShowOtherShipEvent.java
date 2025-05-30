package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.VirtualServer;

public class ShowOtherShipEvent extends ClientEvent{
    private String nickname;
    public ShowOtherShipEvent(String nickname) {
        this.nickname = nickname;
    }
    public String getNicknameRequested() {
        return nickname;
    }
    @Override
    public String getMessage() {
        return "I want to see "+nickname+"'s spaceship";
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
