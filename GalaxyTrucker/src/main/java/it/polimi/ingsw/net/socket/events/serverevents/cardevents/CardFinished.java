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
    ArrayList<Trucker> truckersList = new ArrayList<>();
    public CardFinished(boolean forAll, ArrayList<Trucker> truckerList) {
        super(ServerEventType.CARD_FINISHED, forAll, ParserState.CARD_FINISH, null);
        this.truckersList = truckerList;
    }

    public ArrayList<Trucker> getTruckersList() {
        return truckersList;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
