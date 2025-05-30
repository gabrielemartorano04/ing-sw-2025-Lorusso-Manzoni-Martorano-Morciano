package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

public class SetWinDecision extends ClientEvent {

    private boolean win;

    public SetWinDecision(boolean win) {

        this.win = win;
    }

    public boolean getWin() {
        return win;
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
