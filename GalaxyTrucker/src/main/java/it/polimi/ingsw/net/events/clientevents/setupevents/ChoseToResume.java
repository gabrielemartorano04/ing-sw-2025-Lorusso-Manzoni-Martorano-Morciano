package it.polimi.ingsw.net.events.clientevents.setupevents;

import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;

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
    public void call(VirtualServer visitor) {
        visitor.visit(this);
    }
}
