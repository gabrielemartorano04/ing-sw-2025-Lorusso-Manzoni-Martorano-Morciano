package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an event where a player is presented with an option to gain cosmic credits
 * at the cost of losing days in the game's progression
 */
public class GainCosmicCredits extends CardEvent{

    /**
     * Credits to gain
     */
    private final int cosmicCredits;

    /**
     * Days to lose
     */
    private final int daysLost;

    /**
     * Event constructor
     * @param forAll specify if the message is for a player or everyone
     * @param nickname of the player
     * @param cosmicCredits to gain
     * @param daysLost days to lose
     */
    public GainCosmicCredits(boolean forAll, String nickname, int cosmicCredits, int daysLost) {
        super(forAll, ParserState.COSMIC_CREDITS, nickname,new ArrayList<>());
        this.cosmicCredits = cosmicCredits;
        this.daysLost = daysLost;
    }

    /**
     * @return the credits to gain
     */
    public int getCosmicCredits() {
        return cosmicCredits;
    }

    /**
     * @return the days to lose
     */
    public int getDaysLost() {
        return daysLost;
    }

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers() {
        return getNickname()+" is deciding to gain cosmic credits or not.\n";
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
