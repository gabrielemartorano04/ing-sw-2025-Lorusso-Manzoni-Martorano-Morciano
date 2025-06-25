package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents a warzone card event that indicates the current condition of a game instance
 */
public class CurrentCondition extends CardEvent{

    /**
     * Current condition of the card
     */
    private final Condition condition;

    /**
     * Positions of the players
     */
    private final Map<String, Integer> track;

    /**
     * Weakest player according to the card conditions
     */
    private final Trucker weakestTrucker;

    /**
     * List of withdrawn or removed players
     */
    private final ArrayList<Trucker> truckersRemoved;

    /**
     * EVent constructor
     * @param forAll specify if the message is for a player or everyone
     * @param parserState state of the input parser
     * @param nickname of the player
     * @param condition of the warzone card
     * @param track positions of the players
     * @param weakestTrucker according to the condition
     * @param deadTruckersName list of withdrawn or removed players
     */
    public CurrentCondition(boolean forAll, ParserState parserState, String nickname, Condition condition, Map<String, Integer> track, Trucker weakestTrucker, ArrayList<Trucker> deadTruckersName) {
        super(forAll, parserState, nickname);
        this.condition = condition;
        this.track=track;
        this.weakestTrucker =weakestTrucker;
        this.truckersRemoved=deadTruckersName;
    }

    /**
     * @return the condition of the card
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * @return the positions of the players
     */
    public Map<String, Integer> getTrack() {
        return track;
    }

    /**
     * @return the list of withdrawn or removed players
     */
    public ArrayList<Trucker> getTruckersRemoved(){
        return this.truckersRemoved;
    }

    /**
     * @return the weakest trucker according to the condition
     */
    public Trucker getWeakestTrucker() {
        return weakestTrucker;
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
