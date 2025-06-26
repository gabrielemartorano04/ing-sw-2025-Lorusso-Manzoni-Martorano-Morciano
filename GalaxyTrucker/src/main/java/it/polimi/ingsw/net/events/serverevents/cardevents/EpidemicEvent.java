package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents an event triggered by an epidemic card interaction in the game
 */
public class EpidemicEvent extends CardEvent {

    /**
     * Description of the card
     */
    private final String cardDescription;

    /**
     * Name of the card
     */
    private final String cardName;

    /**
     * List of removed players
     */
    private final ArrayList<Trucker> truckersRemoved;

    /**
     * Map of truckers' positions
     */
    private final Map<String, Integer> track;

    /**
     * Epidemic card
     */
    private final Card card;

    /**
     * Event constructor
     * @param cardName name of the card
     * @param cardDescription description of the card
     * @param forAll specify if the message is for a player or everyone
     * @param truckers list of players
     * @param nickname of the player
     * @param truckersRemoved list of removed players
     * @param track map o truckers' positions
     * @param card epidemic card
     */
    public EpidemicEvent(String cardName, String cardDescription , boolean forAll, ArrayList<Trucker> truckers, String nickname, ArrayList<Trucker> truckersRemoved, Map<String, Integer> track, Card card) {
        super(forAll, ParserState.DRAW_CARD, nickname, truckers);
        this.cardDescription = cardDescription;
        this.cardName = cardName;
        this.track=track;
        this.truckersRemoved = truckersRemoved;
        this.card = card;
    }

    /**
     * @return the card description
     */
    public String getCardDescription() {
        return cardDescription;
    }

    /**
     * @return the card name
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * @return the list of removed players
     */
    public ArrayList<Trucker> getTruckersRemoved(){
        return truckersRemoved;
    }

    /**
     * @return the map of truckers' positions
     */
    public Map<String, Integer> getTrack(){
        return track;
    }

    /**
     * @return the epidemic card
     */
    public Card getCard() {
        return card;
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
