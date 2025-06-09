package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

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
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }
}
