package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;

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
    private ArrayList<int[]> coordinates; //è la lista di coordinate da cui rimuovere gli umani che è settata dal controller

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
     * @param truckerThatHasDecided trucker ID
     */
    public void setTruckerThatHasDecided(Trucker truckerThatHasDecided) {
        this.truckerThatHasDecided = truckerThatHasDecided;
    }

    /**
     * Sets the coordinates of the cabins from which he wants to remove the crew members required
     * @param coordinates list of arrays of coordinates of the cabins
     */
    public void setCoordinates(ArrayList<int[]> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        if (truckerThatHasDecided != null) {
            truckerThatHasDecided.getSpaceShip().removeNumHuman(coordinates);
            game.moveBackward(truckerThatHasDecided,days);
            truckerThatHasDecided.addCosmicCredit(credits);
        }
    }
}
