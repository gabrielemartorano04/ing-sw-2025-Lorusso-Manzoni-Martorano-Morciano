package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

public class StarDustEvent extends CardEvent {

    private final ArrayList<Trucker> truckers;
    private final String cardDescription;
    private final String cardName;
    private final ArrayList<Trucker> truckersRemoved;
    private final Map<String, Integer> track;
    private final Card card;

    public StarDustEvent (String cardName, String cardDescription, boolean forAll, ArrayList<Trucker> turckerList, String nickname, ArrayList<Trucker> truckersRemoved, Map<String, Integer> track, Card card) {
        super(ServerEventType.CARD_FINISHED, forAll, ParserState.DRAW_CARD, nickname);
        this.truckers = turckerList;
        this.cardName = cardName;
        this.cardDescription = cardDescription;
        this.truckersRemoved = truckersRemoved;
        this.track = track;
        this.card = card;
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

    public ArrayList<Trucker> getTruckersRemoved(){
        return truckersRemoved;
    }

    public Map<String, Integer> getTrack(){
        return track;
    }

    public Card getCard() {
        return card;
    }
}
