package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.Trucker;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that manages the single card smugglers
 * In order of position, starting from the leader, each trucker decides which double cannon wants to activate, using batteries, for the calculation of the cannon power.
 * The first trucker that has a cannon power which is higher than the smugglers one, defeat them and decide if he/she wants, losing days of flight, to gain goods.
 * If the trucker's cannon power is equal to the smugglers' one, nothing happens to the trucker, but the smugglers continue to attack
 * For all the defeated truckers, they have to lose some of their most valuable goods.
 */
public class Smugglers extends Card {
    private final int smugglersPower;
    private final int goodsLost;
    private final int days;
    private Goods[] goodsGained = new Goods[3];
    private boolean isDefeated = false;
    private Trucker winnerTrucker = null; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private ArrayList<Trucker> defeatedTruckers;
    ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveBatteries; //solita Lista di trucker di liste di coordinate per tenere traccia per ogni trucker delle coordinate dei depositi da cui rimuovere il numero di batterie contenuto nella carta Smugglers
    ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveGoods; //solita Lista di trucker di liste di coordinate per tenere traccia per ogni trucker delle coordinate dei depositi da cui rimuovere il numero di merci contenuto nella carta Smugglers
    ArrayList<int[]> winnerCoordinatesToAddGoods; //coordinate dei load in cui il vincitore ha scelto di caricare le merci
    private CardState cardState;
    private ArrayList<ArrayList<int[]>> doubleCannonsCoord; // coordinates of the double cannons the trucker wants to activate
    private int playersDecisions; // number of players that gave a choice
    private boolean choiceToGain;
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
        this.doubleCannonsCoord= new ArrayList<>();
        this.cardState = CardState.ATTESA_RISPOSTE;
        this.playersDecisions = 0;
        this.choiceToGain = false;
    }

    /**
     * Returns the cannon power required
     * @return cannon power required
     */
    public int getSmugglersPower() {
        return smugglersPower;
    }

    /**
     * Returns the number of goods to lose
     * @return goods to lose
     */
    public int getGoodsLost() {
        return goodsLost;
    }

    /**
     * Returns an array of goods to gain
     * @return array of goods to gain
     */
    public Goods[] getGoodsGained() {
        return goodsGained;
    }

    /**
     * Returns the number of days of flight to lose
     * @return days to lose
     */
    public int getDays() {
        return days;
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
     * Sets the coordinates of the loads where the winner trucker wants to add the goods gained
     * @param winnerCoordinatesToAddGoods list of coordinates
     */
    public void setWinnerCoordinatesToAddGoods(ArrayList<int[]> winnerCoordinatesToAddGoods) throws IllegalStateException, NotGoodsDepositException {
        if (cardState != CardState.GOODSTOADD) {
            throw new IllegalStateException("Card state is not GOODSTOADD");
        }
        try {
            this.winnerCoordinatesToAddGoods = winnerCoordinatesToAddGoods;
        } catch (NotGoodsDepositException e) {
            throw new NotGoodsDepositException("Not goods deposit");
        }
        cardState = CardState.ESECUZIONE;
    }

    public void addDoubleCannonsCoord(ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker) throws IllegalStateException, NotDoubleCannonException{
        if (cardState != CardState.ATTESA_RISPOSTE) {
            throw new IllegalStateException("Card state is not ATTESA_RISPOSTE");
        }
        try {
            this.doubleCannonsCoord.addLast(doubleCannonsCoord);
        } catch (NotDoubleCannonException e) {
            throw new NotDoubleCannonException("Not DoubleCannonException");
        }
        playersDecisions++;
        if (trucker.getSpaceShip().getTotalCannonPower(doubleCannonsCoord) > smugglersPower) {
            winnerTrucker = trucker;
            cardState = CardState.GAIN;
        }else if(trucker.getSpaceShip().getTotalCannonPower(doubleCannonsCoord) == smugglersPower){
            cardState = CardState.ATTESA_RISPOSTE;
        }else {
            defeatedTruckers.add(trucker);
            if (playersDecisions == numPlayer) {
                cardState = CardState.ESECUZIONE;
            }else cardState = CardState.GOODSTOREMOVE;
        }
    }

    /**
     * Sets, for each trucker, the coordinates of the loads where he/she wants to remove goods if defeated
     * @param truckersCoordinatesToRemoveBatteries list of lists of coordinates
     */
    public void setTruckersCoordinatesToRemoveBatteries(ArrayList<int[]> truckersCoordinatesToRemoveBatteries, int numPlayer, Trucker trucker) {
        if (cardState != CardState.BATTERIESTOREMOVE) {
            throw new IllegalStateException("Card state in not HUMANSTOREMOVE");
        }
        if(trucker.getSpaceShip().getBatteries)
        this.truckersCoordinatesToRemoveBatteries.add(truckersCoordinatesToRemoveBatteries);
        if (playersDecisions == numPlayer){
            cardState = CardState.ESECUZIONE;
        }else{
            cardState = CardState.ATTESA_RISPOSTE;
        }
    }

    public void setTruckersCoordinatesToRemoveGoods(ArrayList<int[]> truckersCoordinatesToRemoveGoods, int numPlayer, Trucker trucker) {
        if (cardState != CardState.GOODSTOREMOVE) {
            throw new IllegalStateException("Card state in not GOODSTOREMOVE");
        }
        if (trucker.getSpaceShip().getGoodsList().size() < goodsLost) {
            if (truckersCoordinatesToRemoveGoods.size() == goodsLost){
                try {
                    try {
                        this.truckersCoordinatesToRemoveGoods.add(truckersCoordinatesToRemoveGoods);
                    } catch (NotHigherGoodException e) {
                        throw new NotHigherGoodException("Not HigherGoodException");
                    }
                } catch (NotGoodsDepositException e) {
                    throw new NotGoodsDepositException("Not GoodsDepositException");
                }
            }else {
                cardState = CardState.GOODSTOREMOVE;
                throw new NotEnoughDepositsException("");
            }
        }else {
            trucker.getSpaceShip().removeAllGoods();
            cardState = CardState.BATTERIESTOREMOVE;
            throw new NotEnoughGoodsException("NotEnoughGoods");
        }
        if (playersDecisions == numPlayer){
            cardState = CardState.ESECUZIONE;
        }else{
            cardState = CardState.ATTESA_RISPOSTE;
        }
    }

    public void setChooseToGain(Boolean choice) throws IllegalStateException {
        if (cardState != CardState.GAIN) {
            throw new IllegalStateException("Card state is not GAIN");
        }
        this.choiceToGain = choice;
        cardState = CardState.GOODSTOADD;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        if (cardState != CardState.ESECUZIONE) {
            throw new IllegalStateException("Card state is not ESECUZIONE");
        }
        int i = 0;
        if(winnerTrucker != null && choiceToGain){
            winnerTrucker.getSpaceShip().addGoods(Arrays.asList(goodsGained), winnerCoordinatesToAddGoods);
            game.moveBackward(winnerTrucker, days);
        }
        if(!defeatedTruckers.isEmpty()){
            for(Trucker trucker: defeatedTruckers){
                if (trucker.getSpaceShip().getGoodsList() == null) {
                        trucker.getSpaceShip().removeBatteries(truckersCoordinatesToRemoveBatteries.get(i));
                }else {
                        trucker.getSpaceShip().removeGoods(truckersCoordinatesToRemoveGoods.get(i));
                    i++;
                }
            }
        }
    }
}
