package it.polimi.ingsw.net.events.clientevents.setupevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class ChoseToResume extends ClientEvent {

    private boolean choseToResume;

    public ChoseToResume(boolean choseToResume) {
        super();
        this.choseToResume = choseToResume;
    }

    public boolean getChoseToResume() {
        return choseToResume;
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
        return visitor.visit(this);
    }
}
