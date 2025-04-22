package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.visitor.CardVisitor;
import it.polimi.ingsw.model.visitor.ParametricCardVisitor;

import java.util.ArrayList;

/**
 * Class that manages the single card abandoned ship
 * In order of position, starting from the leader, the trucker decides, checking the crew members requirement, if he/she wants to lose members of the crew and days of flight in exchange for cosmic credits.
 * Only one trucker car take the station and is possible that nobody wants to take it
 */
public class AbandonedShip extends Card {
    private final int days;
    private final int credits;
    private final int numHuman;
    private Trucker truckerThatHasDecided = null; //è il trucker che ha deciso di atterrare, è null se nessuno ha deciso di atterrare
    private ArrayList<int[]> coordinates = null; //è la lista di coordinate da cui rimuovere gli umani che è settata dal controller
    private CardState state = CardState.ATTESA_RISPOSTE;
    private int playerDecisions;

    /**
     * Abandoned ship constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param numHuman number of crew members to lose
     * @param credits number of cosmic credits gain
     * @param days number of days of flight to lose
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public AbandonedShip(boolean trialFlight, int level, int numHuman, int credits, int days, String frontImage, String backImage) {
        super("Abandoned Ship", "A drifting ghost ship",  trialFlight, level, frontImage, backImage);
        this.numHuman = numHuman;
        this.credits = credits;
        this.days = days;
        this.truckerThatHasDecided = null;
        this.playerDecisions = 0;
        this.state = CardState.ATTESA_RISPOSTE;
    }

    @Override
    public Object accept(CardVisitor visitor) {
        return visitor.visitForAbandonedShip(this);
    }

    @Override
    public Object accept(ParametricCardVisitor visitor, Object... params) {
        return visitor.visitForAbandonedShip(this, params);
    }

    /**
     * Returns the number of days of flight to lose
     * @return days to lose
     */
    public int getDays() {
        return days;
    }

    /**
     * Returns the number of cosmic credits to gain
     * @return credit to gain
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Returns the number of crew members to lose
     * @return crew members to lose
     */
    public int getNumHuman() {
        return numHuman;
    }

    /**
     * Sets the only trucker that has chosen to take the ship
     * @param trucker trucker ID
     */
    public CardState setTruckerThatHasDecided(Trucker trucker, boolean landing, int numPlayer)  {
        if(state != CardState.ATTESA_RISPOSTE) {
            throw new IllegalStateException("SetTruckerThatHasDecided not callable in state: " + state);
        }
        if(landing) {
            truckerThatHasDecided = trucker;
            state = CardState.HUMANSTOREMOVE;
        }else{
            if (playerDecisions == numPlayer){
                state = CardState.ESECUZIONE;
            }else{
                state = CardState.ATTESA_RISPOSTE;
            }
        }
        playerDecisions++;
        return state;
    }

    /**
     * Sets the coordinates of the cabins from which he wants to remove the crew members required
     * @param coordinates list of arrays of coordinates of the cabins
     */
    public CardState setCoordinates(ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates {
        if(state != CardState.HUMANSTOREMOVE) {
            throw new IllegalStateException("Can't call setCoordinates in the state: " + state);
        }
        if(coordinates==null|| coordinates.isEmpty()){
            throw new InvalidCoordinates("Coordinates is null or empty");
        }
        try{
            truckerThatHasDecided.getSpaceShip().checkCabin(coordinates, numHuman);
        }catch (InvalidCoordinates e){
            throw new InvalidCoordinates(e.getMessage());
        }
        this.coordinates = coordinates;

        if(truckerThatHasDecided!=null){
            state = CardState.ESECUZIONE;
        }else{
            throw new RuntimeException("Tried to set coordinates when truckerThatHasDecided is null");
        }
        return state;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */

    @Override
    public synchronized CardState cardEffect(Game game) {
        if(state != CardState.ESECUZIONE) {
            throw new IllegalStateException("Non puoi settare fare CardEffect nello stato: " + state);
        }
        if (truckerThatHasDecided != null) {
            truckerThatHasDecided.getSpaceShip().removeNumHuman(coordinates);
            game.moveBackward(truckerThatHasDecided,days);
            truckerThatHasDecided.addCosmicCredit(credits);
        }
        state = CardState.EXIT;
        return state;
    }
}
