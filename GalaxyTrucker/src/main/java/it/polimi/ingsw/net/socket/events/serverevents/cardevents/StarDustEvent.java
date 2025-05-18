package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class StarDustEvent extends ServerEvent {

    private ArrayList<Trucker> truckers;

    public StarDustEvent (boolean forAll, CardState state, ArrayList<Trucker> turckerList) {
        super(ServerEventType.CARD_FINISHED, forAll);
        this.truckers = turckerList;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {

    }

    public ArrayList<Trucker> getTruckers() {
        return truckers;
    }
}
