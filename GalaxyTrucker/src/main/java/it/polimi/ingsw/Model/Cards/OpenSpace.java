package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.List;


/**
 * In order of position, starting from the leader, each trucker decides which double engine wants to activate, using batteries, for the calculation of the total engine power.
 * The total engine power of the trucker is the number of days of flight that his/her spaceship will acquire on the dashboard
 */
public class OpenSpace extends Card {
    private ArrayList<ArrayList<int[]>> truckersCoordinates;
    //lista che contiene per ogni trucker la lista di coordinates dei motori doppi che vuole attivare
    //la lista esterna associa ogni puntatore a un trucker, quindi la lunghezza della lista esterna è quella di truckers.
    //mantiene l'ordine dei truckers nel fare questa associazione

    /**
     * Open space constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public OpenSpace(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Open Space", "Empty vastness of space", trialFlight, level, frontImage, backImage);
    }

    /**
     * Sets the coordinates of the double cannon that each trucker wants to activate
     * @param truckersCoordinates list of lists of arrays of coordinates
     */
    public void setCoordinates(ArrayList<ArrayList<int[]>> truckersCoordinates) {
        this.truckersCoordinates = truckersCoordinates;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        List<Trucker> truckers = game.getTruckerList();
        int[] enginePowerPerTrucker = new int[truckers.size()];
        int i = 0;
        for (Trucker trucker : truckers) {
            enginePowerPerTrucker[i] = trucker.getSpaceShip().getTotalEnginePower(truckersCoordinates.get(i));
            if(enginePowerPerTrucker[i] == 0){
                game.withdraw(trucker);
            }
            i++;
        }
        for (i = 0; i < enginePowerPerTrucker.length; i++) {
            game.moveForward(truckers.get(i), enginePowerPerTrucker[i]);
        }
    }
}

