package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Arrays;

//secondo me tutti i get sono inutil

/**
 * Class that manages the single card smugglers
 *
 */
public class Smugglers extends Card {
    private final int smugglersPower;
    private final int goodsLost;
    private final int days;
    private Goods[] goodsGained = new Goods[3];
    private boolean isDefeated = false;
    private Trucker winnerTrucker = null; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private ArrayList<Trucker> defeatedTruckers;
    ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveGoods; //solita Lista di trucker di liste di coordinate per tenere traccia per ogni trucker delle coordinate dei Load da cui rimuovere il numero di merci contenuto nella carta Smugglers
    ArrayList<int[]> winnerCoordinatesToAddGoods; //coordinate dei load in cui il vincitore ha scelto di caricare le merci

    /**
     * Smugglers constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param smugglersPower cannon power required
     * @param goodsLost number of goods to lose
     * @param days number of days of flight to lose
     * @param goodsGained arrays of goods to gain
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Smugglers(boolean trialFlight, int smugglersPower, int goodsLost, int days, Goods[] goodsGained, int level, String frontImage, String backImage) {
        super("Smugglers", "Illegal traders of goods", trialFlight, level, frontImage, backImage);
        this.smugglersPower = smugglersPower;
        this.goodsLost = goodsLost;
        this.days = days;
        this.goodsGained = goodsGained;
    }

    public int getSmugglersPower() {
        return smugglersPower;
    }

    public int getGoodsLost() {
        return goodsLost;
    }

    public Goods[] getGoodsGained() {
        return goodsGained;
    }

    public int getDays() {
        return days;
    }

    public void setDefeatedTruckers(ArrayList<Trucker> defeatedTruckers) {
        this.defeatedTruckers = defeatedTruckers;
    }

    public void setWinnerTrucker(Trucker winnerTrucker) {
        this.winnerTrucker = winnerTrucker;
    }

    public void setWinnerCoordinatesToAddGoods(ArrayList<int[]> winnerCoordinatesToAddGoods) {
        this.winnerCoordinatesToAddGoods = winnerCoordinatesToAddGoods;
    }

    public void setTruckersCoordinatesToRemoveGoods(ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveGoods) {
        this.truckersCoordinatesToRemoveGoods = truckersCoordinatesToRemoveGoods;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        int i = 0;
        if(winnerTrucker != null){
            winnerTrucker.getSpaceShip().addGoods(Arrays.asList(goodsGained), winnerCoordinatesToAddGoods);
            game.moveBackward(winnerTrucker, days);
        }
        if(!defeatedTruckers.isEmpty()){
            for(Trucker trucker: defeatedTruckers){
                trucker.getSpaceShip().removeGoods(goodsLost, truckersCoordinatesToRemoveGoods.get(i));
                i++;
            }
        }
    }


}
