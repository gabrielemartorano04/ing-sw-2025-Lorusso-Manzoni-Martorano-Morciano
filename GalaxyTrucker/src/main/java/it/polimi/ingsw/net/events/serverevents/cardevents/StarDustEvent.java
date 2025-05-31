package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

public class StarDustEvent extends CardEvent {

    private ArrayList<Trucker> truckers;
    private String cardDescription;
    private String cardName;
    private ArrayList<String> truckersRemoved;
    private Map<String, Integer> track;

    public StarDustEvent (String cardName, String cardDescripition, boolean forAll, ArrayList<Trucker> turckerList, String nickname, ArrayList<String> truckersRemoved, Map<String, Integer> track) {
        super(ServerEventType.CARD_FINISHED, forAll, ParserState.DRAW_CARD, nickname);
        this.truckers = turckerList;
        this.cardName = cardName;
        this.cardDescription = cardDescription;
        this.truckersRemoved = truckersRemoved;
        this.track = track;
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

    public ArrayList<String> getTruckersRemoved(){
        return truckersRemoved;
    }

    public Map<String, Integer> getTrack(){
        return track;
    }
}
