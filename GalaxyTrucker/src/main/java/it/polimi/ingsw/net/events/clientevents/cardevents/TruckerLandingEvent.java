package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

public class TruckerLandingEvent extends ClientEvent {

    private boolean landing;

    public TruckerLandingEvent(boolean landing) {

        this.landing = landing;
    }

    public boolean getLanding() {
        return landing;
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
