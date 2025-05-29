package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CardFinished extends CardEvent {
    private ArrayList<Trucker> truckersList = new ArrayList<>();
    private ArrayList<Trucker> truckersRemoved = new ArrayList<>();
    public CardFinished(boolean forAll, ArrayList<Trucker> truckerList, ArrayList<Trucker> truckerRemoved) {
        super(ServerEventType.CARD_FINISHED, forAll, ParserState.DRAW_CARD, null);
        this.truckersList = truckerList;
        this.truckersRemoved = truckerRemoved;
    }

    public ArrayList<Trucker> getTruckersList() {
        return truckersList;
    }

    public ArrayList<Trucker> getTruckersRemoved(){
        return truckersRemoved;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
