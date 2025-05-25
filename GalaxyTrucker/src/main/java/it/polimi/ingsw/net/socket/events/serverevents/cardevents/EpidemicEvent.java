package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class EpidemicEvent extends CardEvent {

    private ArrayList<Trucker> truckers;
    private String cardDescription;
    private String cardName;

    public EpidemicEvent(String cardName, String cardDescripition ,boolean forAll, ArrayList<Trucker> turckerList, String nickname) {
        super(ServerEventType.CARD_FINISHED, forAll, ParserState.DRAW_CARD, nickname);
        this.truckers = turckerList;
        this.cardDescription = cardDescripition;
        this.cardName = cardName;
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
