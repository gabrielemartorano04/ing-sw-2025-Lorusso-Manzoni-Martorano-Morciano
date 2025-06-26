package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents a specific type of card event
 */
public class StarDustEvent extends CardEvent {

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
     * StarDust card
     */
    private final Card card;

    /**
     * Event constructor
     * @param cardName of the card
     * @param cardDescription description of the card
     * @param forAll specify if the message is for a player or everyone
     * @param turckerList list of players
     * @param nickname of the player
     * @param truckersRemoved list of removed players
     * @param track map of the truckers' positions
     * @param card starDust card
     */
    public StarDustEvent (String cardName, String cardDescription, boolean forAll, ArrayList<Trucker> turckerList, String nickname, ArrayList<Trucker> truckersRemoved, Map<String, Integer> track, Card card) {
        super(forAll, ParserState.DRAW_CARD, nickname,turckerList);
        this.cardName = cardName;
        this.cardDescription = cardDescription;
        this.truckersRemoved = truckersRemoved;
        this.track = track;
        this.card = card;
    }

    /**
     * @return the description of the card
     */
    public String getCardDescription() {
        return cardDescription;
    }

    /**
     * @return the name of the card
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
     * @return the map of tuckers' positions
     */
    public Map<String, Integer> getTrack(){
        return track;
    }

    /**
     * @return the starDust card
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
