package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class EpidemicEvent extends ServerEvent {

    private ArrayList<Trucker> truckers;
    private String cardDescription;
    private String cardName;

    public EpidemicEvent(String cardName, String cardDescripition ,boolean forAll, CardState state, ArrayList<Trucker> turckerList) {
        super(ServerEventType.CARD_FINISHED, forAll);
        this.truckers = turckerList;
        this.cardDescription = cardDescripition;
        this.cardName = cardName;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {

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
