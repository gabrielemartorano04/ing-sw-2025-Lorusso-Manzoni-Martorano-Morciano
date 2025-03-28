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
    private CardState state;
    private int playersLandedIndex = 0;

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
        this.truckersCoordinates = new ArrayList<>();
    }

    /**
     * Returns, for each planet on the card, a list of goods to gain
     * @return list of lists of good to gain
     */
    public ArrayList<ArrayList<Goods>> getPlanets() {
        return planets;
    }

    /**
     * Sets, for each trucker, the coordinates of the loads where he/she wants to add the goods gained
     * @param truckerCoordinate list of arrays of coordinates
     */
    public void setTruckersCoordinates(ArrayList<int[]> truckerCoordinate, int numTruckers) {
        if (this.state != CardState.ATTESA_RISPOSTE) {
            throw new IllegalStateException("Non è possibile inserire le informazioni nello stato " + this.state);
        }
        this.truckersCoordinates.addLast(truckerCoordinate);
        if (truckersCoordinates != null && truckersCoordinates.size() == numTruckers) {
            boolean allValid = true;
            for (ArrayList<int[]> list : truckersCoordinates) {
                if (list == null || list.isEmpty()) {
                    allValid = false;
                    break;
                }
            }
            if (allValid) {
                state = CardState.ESECUZIONE;
            }
        }
    }

    /**
     * Sets which truckers landed on a planet
     * @param playerLanded specify if the trucker
     * @param numTruckers number of truckers
     */
    public void setPlayersLanded(int playerLanded, int numTruckers) {
        if (this.state != CardState.ATTESA_RISPOSTE) {
            throw new IllegalStateException("Non è possibile inserire le informazioni nello stato " + this.state);
        }
        if (this.playersLanded == null) {
            this.playersLanded = new int[numTruckers];
        }
        while (playersLandedIndex < this.playersLanded.length && this.playersLanded[playersLandedIndex] != 0) {
            playersLandedIndex++;
        }
        this.playersLanded[playersLandedIndex] = playerLanded;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        if (this.state != CardState.ESECUZIONE) {
            throw new IllegalStateException("Non è possibile eseguire card effect nello stato " + this.state);
        }
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
        state = CardState.ATTESA_RISPOSTE;
    }
}