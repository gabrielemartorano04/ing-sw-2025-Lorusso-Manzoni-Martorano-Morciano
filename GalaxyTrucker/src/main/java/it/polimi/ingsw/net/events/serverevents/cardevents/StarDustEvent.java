package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

public class StarDustEvent extends CardEvent {

    private final ArrayList<Trucker> truckers;
    private final String cardDescription;
    private final String cardName;
    private final ArrayList<String> truckersRemoved;
    private final Map<String, Integer> track;
    private final String frontImagePath;

    public StarDustEvent (String cardName, String cardDescription, boolean forAll, ArrayList<Trucker> turckerList, String nickname, ArrayList<String> truckersRemoved, Map<String, Integer> track, String frontImagePath) {
        super(ServerEventType.CARD_FINISHED, forAll, ParserState.DRAW_CARD, nickname);
        this.truckers = turckerList;
        this.cardName = cardName;
        this.cardDescription = cardDescription;
        this.truckersRemoved = truckersRemoved;
        this.track = track;
        this.frontImagePath = frontImagePath;
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

    public String getFrontImagePath() {
        return frontImagePath;
    }
}
