package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;

/**
 * Class that manages the single card planets
 * In order of position, starting from the leader, each trucker decides if he/she wants, losing days of flight, to land on a planet and which.
 * Once everybody has decided, in reverse order of position, the truckers lose their days of flight.
 * Is possible that nobody wants to land on a planet.
 */
public class Planets extends Card {
    private final ArrayList<ArrayList<Goods>> planets;
    private final int daysLoss;
    private int[] playersLanded; // viene settato dal Card Controller e playersLanded[i] vale 1 se quel player è atterato se no 0
    private ArrayList<ArrayList<int[]>> truckersCoordinates; // solita convenzione per salvarmi le coordinate di dove aggiungere le merci per ogni trucker

    /**
     * Planets constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param daysLoss number of days of flight to lose
     * @param planets list of lists of goods to gain
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Planets(boolean trialFlight, int daysLoss, int level, ArrayList<ArrayList<Goods>> planets, String frontImage, String backImage) {
        super("Planets", "A new planetary system discovered", trialFlight, level, frontImage, backImage);
        this.daysLoss = daysLoss;
        this.planets = planets;
    }

    /**
     * Returns, for each planet on the card, a list of goods to gain
     * @return list of lists of good to gain
     */
    public ArrayList<ArrayList<Goods>> getPlanets() {
        return planets;
    }

    /**
     * Sets, for each trucker, the coordinates of the loads where he/she want to add the goods gain
     * @param truckersCoordinates list of lists of coordinates
     */
    public void setTruckersCoordinates(ArrayList<ArrayList<int[]>> truckersCoordinates) {
        this.truckersCoordinates = truckersCoordinates;
    }

    /**
     * Sets which truckers landed on a planet
     * @param playersLanded array of landed truckers
     */
    public void setPlayersLanded(int[] playersLanded) {
        this.playersLanded = playersLanded;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        int i = 0;
        ArrayList<Trucker> truckers = game.getTruckerList();

        // riempo le merci a tutti tanto chi non è atterrato su un pianeta o non ha voluto caricare nessuna merce
        // avrà truckersCoordinates.get(i) come lista vuota e addGoods non fa nulla
        for(Trucker trucker : truckers){
            trucker.getSpaceShip().addGoods(planets.get(i), truckersCoordinates.get(i));
            i++;
        }

        // muovo in dietro i giocatori ma partendo dal più indietro nella plancia non dal Leader
        for(i = playersLanded.length - 1; i>=0; i--){
            if(playersLanded[i] == 1){
                game.moveBackward(truckers.get(i), daysLoss);
            }
        }
    }
}