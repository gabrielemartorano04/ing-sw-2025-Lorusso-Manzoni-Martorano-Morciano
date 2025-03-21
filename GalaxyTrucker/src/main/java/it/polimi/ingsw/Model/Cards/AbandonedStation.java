package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;
import it.polimi.ingsw.Model.Components.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages the single card abandoned ship
 * In order of position, starting from the leader, the trucker decides, checking the crew members requirement, if he/she wants to lose days of flight in exchange for goods.
 * Only one trucker car take the station and is possible that nobody wants to take it
 */
public class AbandonedStation extends Card {
    private final int days;
    private final int numHuman;
    private final ArrayList<Goods> goods;
    private Trucker truckerThatHasDecided = null; //è il trucker che ha deciso di atterrare, è null se nessuno ha deciso di atterrare
    private ArrayList<int[]> coordinates; //coordinates sono le coordinate dei Load dove inserire le merci. Viene settato dal Card controller, se è vuoto vuol dire che nessun giocatore ha voluto accettare la carta

    /**
     * Abandoned station constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param goods list of goods to acquire
     * @param days number of days of flight to lose
     * @param numHuman number of crew members required
     * @param frontImage front image of the card
     * @param backIMage back image of the card
     */
    public AbandonedStation(boolean trialFlight, int level, ArrayList<Goods> goods, int days, int numHuman,String frontImage, String backIMage) {
        super("Abandoned Station", "A derelict space station", trialFlight, level, frontImage, backIMage);
        this.days = days;
        this.numHuman = numHuman;
        this.goods = goods;
    }

    /**
     * Returns the number of days of flight to lose
     * @return days to lose
     */
    public int getDays() {
        return days;
    }

    /**
     * Returns the number of crew members required
     * @return crew members to lose
     */
    public int getNumHuman() {
        return numHuman;
    }

    /**
     * Returns the list of goods to acquire
     * @return goods to acquire
     */
    public ArrayList<Goods> getGoods() {
        return goods;
    }

    /**
     * Sets the only trucker that has chosen to take the station
     * @param truckerThatHasDecided trucker ID
     */
    public void setTruckerThatHasDecided(Trucker truckerThatHasDecided) {
        this.truckerThatHasDecided = truckerThatHasDecided;
    }

    /**
     * Sets the coordinates of the loads in which he wants to add the goods acquired
     * @param coord list of arrays of coordinates of the loads
     */
    public void setCoord(ArrayList<int[]> coord) {
        this.coordinates = coord;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        truckerThatHasDecided.getSpaceShip().addGoods(goods,coordinates);
        game.moveBackward(truckerThatHasDecided, days);
    }
}