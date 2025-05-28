package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

import java.net.Socket;

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
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
