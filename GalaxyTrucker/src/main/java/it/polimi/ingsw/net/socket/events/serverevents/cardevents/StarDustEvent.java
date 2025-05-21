package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class StarDustEvent extends ServerEvent {

    private ArrayList<Trucker> truckers;
    private String cardDescription;
    private String cardName;

    public StarDustEvent (String cardName, String cardDescripition, boolean forAll, ArrayList<Trucker> turckerList) {
        super(ServerEventType.CARD_FINISHED, forAll);
        this.truckers = turckerList;
        this.cardName = cardName;
        this.cardDescription = cardDescription;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public String getCardName() {
        return cardName;
    }

    public ArrayList<Trucker> getTruckers() {
        return truckers;
    }
}
