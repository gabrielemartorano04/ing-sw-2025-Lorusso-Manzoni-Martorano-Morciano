package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class EpidemicEvent extends ServerEvent {

    private ArrayList<Trucker> truckers;

    public EpidemicEvent(boolean forAll, CardState state, ArrayList<Trucker> turckerList) {
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
