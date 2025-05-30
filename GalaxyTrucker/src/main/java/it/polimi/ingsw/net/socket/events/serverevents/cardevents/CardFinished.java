package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class CardFinished extends CardEvent {
    private final ArrayList<Trucker> truckersList;
    private final ArrayList<String> truckersRemoved;
    public CardFinished(boolean forAll, ArrayList<Trucker> truckerList, ArrayList<String> truckerRemoved) {
        super(ServerEventType.CARD_FINISHED, forAll, ParserState.DRAW_CARD, null);
        this.truckersList = truckerList;
        this.truckersRemoved = truckerRemoved;
    }

    public ArrayList<Trucker> getTruckersList() {
        return truckersList;
    }

    public ArrayList<String> getTruckersRemoved(){
        return truckersRemoved;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
