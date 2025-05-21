package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

import java.util.ArrayList;

public class RemoveAskingEvent extends ClientEvent {

    private ArrayList<Integer> indexes;
    private String nickname;

    public RemoveAskingEvent(ArrayList<Integer> indexes, String nickname) {

        this.indexes = indexes;
        this.nickname = nickname;
    }

    public ArrayList<Integer> getIndexes() {
        return indexes;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
