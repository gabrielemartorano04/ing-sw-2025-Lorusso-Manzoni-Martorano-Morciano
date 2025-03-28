package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;

/**
 * Class that manages the single card slavers
 * In order of position, starting from the leader, each trucker decides which double cannon wants to activate, using batteries, for the calculation of the cannon power.
 * The first trucker that has a cannon power which is equals or higher than the pirates one, defeat them and decide if he/she wants, losing days of flight, to gain cosmic credits.
 * For all the defeated truckers, they have to remove some crew members
 */
public class Slavers extends Card {
    private final int slaversPower;
    private final int credits;
    private final int days;
    private final int astronautsLost;
    private Trucker winnerTrucker = null; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private ArrayList<Trucker> defeatedTruckers;
    private ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveHumans = new ArrayList<>();
    // contiene le coordinate delle cabine scelte dall'utente per rimuovere gli umani. Queste coordinate sono organizzate
    // in liste di liste. La lista esterna è una per ogni trucker, la lista interna contiene la lista di coordinate per quel
    // trucker, mantenute nell'ordine dei trucker.

    /**
     * Slavers constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param credits number of cosmic credit to gain
     * @param days number of days of flight to lose
     * @param astronautsLost number of crew member to lose
     * @param slaversPower cannon power required
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Slavers(boolean trialFlight, int level, int credits, int days, int astronautsLost, int slaversPower, String frontImage, String backImage ) {
        super("Slavers", "Intergalactic slave traders", trialFlight, level, frontImage, backImage);
        this.credits = credits;
        this.days = days;
        this.astronautsLost = astronautsLost;
        this.slaversPower = slaversPower;
    }

    /**
     * Returns the cannon power required
     * @return cannon power required
     */
    public int getSlaversPower() {
        return slaversPower;
    }

    /**
     * Returns the number of crew members to lose
     * @return crew members to lose
     */
    public int getAstronautsLost() {
        return astronautsLost;
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
     * Sets the list of defeated truckers
     * @param defeatedTruckers list of truckers ID
     */
    public void setDefeatedTruckers(ArrayList<Trucker> defeatedTruckers) {
        this.defeatedTruckers = defeatedTruckers;
    }

    /**
     * Sets the trucker that defeat the pirates
     * @param winnerTrucker trucker ID
     */
    public void setWinnerTrucker(Trucker winnerTrucker) {
        this.winnerTrucker = winnerTrucker;
    }

    /**
     * Sets, for each trucker, the coordinates of the cabin where he/she want to remove the crew members
     * @param truckersCoordinatesToRemoveHumans list of lists of arrays of coordinates
     */
    public void setTruckersCoordinatesToRemoveHumans(ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveHumans) {
        this.truckersCoordinatesToRemoveHumans = truckersCoordinatesToRemoveHumans;
    }

    public void addTruckersCoordinatesToRemoveHumans(ArrayList<int[]> truckersCoordinates) {
        this.truckersCoordinatesToRemoveHumans.add(truckersCoordinates);
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        int i = 0;
        if(winnerTrucker!=null){
            winnerTrucker.addCosmicCredit(credits);
            game.moveBackward(winnerTrucker, days);
        }
        if(!defeatedTruckers.isEmpty()) {
            for (Trucker trucker : defeatedTruckers) {
                trucker.getSpaceShip().removeNumHuman(truckersCoordinatesToRemoveHumans.get(i));
                i++;
            }
        }
    }

}