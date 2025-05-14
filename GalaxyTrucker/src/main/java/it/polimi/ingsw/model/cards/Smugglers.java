package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.callers.SmugglersCaller;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.controller.TruckerController;
import it. polimi.ingsw.model.Bank;
import it.polimi.ingsw.utilities.exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Class that manages the single card smugglers
 * In order of position, starting from the leader, each trucker decides which double cannon wants to activate, using batteries, for the calculation of the cannon power.
 * The first trucker that has a cannon power which is higher than the smugglers one, defeat them and decide if he/she wants, losing days of flight, to gain goods.
 * If the trucker's cannon power is equal to the smugglers' one, nothing happens to the trucker, but the smugglers continue to attack
 * For all the defeated truckers, they have to lose some of their most valuable goods.
 */
public class Smugglers extends Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int smugglersPower;
    private int goodsLost;
    private final int days;
    private Goods[] goodsGained = new Goods[3];
    private TruckerController winnerTruckerController = null; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private ArrayList<TruckerController> defeatedTruckerControllers;
    private CardState cardState;
    private ArrayList<ArrayList<int[]>> doubleCannonsCoord; // coordinates of the double cannons the trucker wants to activate
    private int playersDecisions; // number of players that gave a choice
    private int currGood;
    private boolean chooseToGain;
    // variabili per la remove
    private int colorOfCurrGoodToRemove; // vale 0 se è rosso, 1 se è giallo, 2 se è verde e 3 se è blu
    private int numOfCurrTypeOfGood;
    private ArrayList<Goods> goodsToRemove;// verrà creata al più una volta per istanza di questa classe
    // fine variabili per la remove

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
        super("Smugglers", "Illegal traders of goods", trialFlight, level, frontImage, backImage, new SmugglersCaller());
        this.smugglersPower = smugglersPower;
        this.goodsLost = goodsLost;
        this.days = days;
        this.goodsGained = goodsGained;
        this.doubleCannonsCoord= new ArrayList<>();
        this.cardState = CardState.ATTESA_RISPOSTE;
        this.playersDecisions = 0;
        this.currGood=0;
        this.winnerTruckerController =null;
        this.chooseToGain=false;
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
     * @param defeatedTruckerControllers list of truckers ID
     */
    public void setDefeatedTruckers(ArrayList<TruckerController> defeatedTruckerControllers) {
        this.defeatedTruckerControllers = defeatedTruckerControllers;
    }

    /**
     * Sets the trucker that defeat the pirates
     * @param winnerTruckerController trucker ID
     */
    public void setWinnerTrucker(TruckerController winnerTruckerController) {
        this.winnerTruckerController = winnerTruckerController;
    }

    public CardState getCardState() {
        return cardState;
    }

    public CardState addDoubleCannonsCoord(TruckerController truckerController, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Bank bank) throws GeneralError, InvalidComponentsException{
        if (cardState != CardState.ATTESA_RISPOSTE) {
            throw new GeneralError("Card state is not ATTESA_RISPOSTE");
        }
        if (truckerController.getSpaceShip().getNumDoubleCannon()!=0) {
            if (!doubleCannonsCoord.isEmpty() || truckerController.getSpaceShip().checkDoubleCannon(Objects.requireNonNull(doubleCannonsCoord))) {
                this.doubleCannonsCoord.addLast(doubleCannonsCoord);
                playersDecisions++;
            } else {
                throw new InvalidComponentsException("not a double cannon");
            }
        }
        if (truckerController.getSpaceShip().getTotalCannonPower(doubleCannonsCoord) > smugglersPower) {
            winnerTruckerController = truckerController;
            do {
                if (bank.getGoodsInBank(goodsGained[currGood].getValue()) >= 1) {
                    cardState = CardState.REMOVE_GOODS;
                } else {
                    currGood++;
                    if(currGood>= goodsGained.length) {
                        cardState = CardState.REMOVE_GOODS;
                    }
                }
            }while(currGood<goodsGained.length && cardState!=CardState.REMOVE_GOODS);
        }else if(truckerController.getSpaceShip().getTotalCannonPower(doubleCannonsCoord) == smugglersPower){
            if (playersDecisions == numPlayer) {
                playersDecisions=0;
                cardState = CardState.REMOVE_GOODS;
            }else cardState = CardState.ATTESA_RISPOSTE;
        }else {
            defeatedTruckerControllers.add(truckerController);
            if (playersDecisions == numPlayer) {
                playersDecisions=0;
                cardState = CardState.REMOVE_GOODS;
            }else cardState = CardState.ATTESA_RISPOSTE;
        }
        return cardState;
    }

    public Goods plotCurrGood() throws GeneralError{
        if(cardState!=CardState.PLOT_GOOD){
            throw new GeneralError("Card state is not CHOOSE_TO_GAIN");
        }
        cardState = CardState.CHOOSE_TO_GAIN;
        return goodsGained[currGood];
    }

    public CardState setChoiceToGain(boolean choice) throws GeneralError {
        if(cardState!=CardState.CHOOSE_TO_GAIN){
            throw new GeneralError("Card state is not CHOOSE_TO_GAIN");
        }
        if (choice) {
            if (!chooseToGain) {
                this.chooseToGain = choice;
            }
            cardState = CardState.GOODSTOADD;
        }else {
            if(currGood==goodsGained.length-1) {
                if (chooseToGain) {
                    cardState = CardState.REARRANGE;
                }else cardState = CardState.ESECUZIONE;
            } else currGood++;
        }
        return cardState;
    }

    /**
     * Sets the coordinates of the loads where the winner trucker wants to add the goods gained
     * @param winnerCoordinatesToAddGood list of coordinatest
     */
    public CardState setWinnerCoordinatesToAddGoods(int[] winnerCoordinatesToAddGood, int value, TruckerController truckerController, Bank bank) throws GeneralError, InvalidCoordinates  {
        if (cardState != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }
        if (winnerCoordinatesToAddGood==null || !truckerController.getSpaceShip().checkLoad(winnerCoordinatesToAddGood)) { //check load deve controllare il singolo componente
            throw new InvalidCoordinates("Not goods deposit");
        }
        try{
            truckerController.getSpaceShip().addGood(goodsGained[currGood], winnerCoordinatesToAddGood, value, bank);
            bank.removeGoodsInBank(goodsGained[currGood].getValue());
        }catch (InvalidPositionException e) {
            cardState = CardState.GOODSTOADD;
            return cardState;
        }
        if(currGood == goodsGained.length-1){
            cardState = CardState.REARRANGE;
        }
        else{
            currGood++;
            do {
                if (bank.getGoodsInBank(goodsGained[currGood].getValue()) >= 1) {
                    cardState = CardState.PLOT_GOOD;
                } else {
                    currGood++;
                    if(currGood>=goodsGained.length) {
                        cardState = CardState.REARRANGE;
                    }
                }
            }while(currGood<goodsGained.length && cardState!=CardState.PLOT_GOOD);
        }
        return cardState;
    }

    // si potrebbe non metter contiune e procedere se tutto e a null
    public CardState rearrange(TruckerController truckerController, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.REARRANGE) {
            throw new GeneralError("Card state is not REARRANGE");
        }
        if (!contiune) {
            if (coord1 == null && !truckerController.getSpaceShip().checkLoad(coord1) && coord2 == null && !truckerController.getSpaceShip().checkLoad(coord2)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                truckerController.getSpaceShip().swapGoods(coord1, value1, coord2, value2);
            } catch (InvalidPositionException e) {
                cardState = CardState.REARRANGE;
                throw new InvalidPositionException("invalid positions");
            }
        } else{
            cardState = CardState.ESECUZIONE;
        }
        return cardState;
    }

    public CardState manageRemoveGoods(TruckerController truckerController, Bank bank) throws GeneralError{
        if(cardState != CardState.REMOVE_GOODS){
            throw new GeneralError("plotGoodsToRemove not callable in the state " + cardState);
        }
        if(truckerController == null){
            throw new GeneralError("trucker is null");
        }
        ArrayList<Goods> goodsList = truckerController.getSpaceShip().getGoodsList();

        if(!defeatedTruckerControllers.isEmpty()) {
            // se il colore corrente è minore uguale a 3 allora devo togliere le merci
            while (colorOfCurrGoodToRemove <= 3 && goodsLost > 0) {
                switch (colorOfCurrGoodToRemove) {
                    case 0 -> {
                        numOfCurrTypeOfGood = truckerController.getSpaceShip().getRedGoods();
                        colorOfCurrGoodToRemove++;
                    }
                    case 1 -> {
                        numOfCurrTypeOfGood = truckerController.getSpaceShip().getYellowGoods();
                        colorOfCurrGoodToRemove++;
                    }
                    case 2 -> {
                        numOfCurrTypeOfGood = truckerController.getSpaceShip().getGreenGoods();
                        colorOfCurrGoodToRemove++;
                    }
                    case 3 -> {
                        numOfCurrTypeOfGood = truckerController.getSpaceShip().getBlueGoods();
                        colorOfCurrGoodToRemove++;
                    }
                }

                if (numOfCurrTypeOfGood <= goodsLost) {
                    removeWithoutAsking(truckerController, goodsList, bank);
                } else {
                    cardState = CardState.PLOT_GOODSLIST;
                    break;
                }
            }
            if (colorOfCurrGoodToRemove > 3 && goodsLost > 0) {
                // se il colore corrente è maggiore di 3 allora vuol dire che devo togliere batterie se ho ancora merci da rimuovere
                cardState = CardState.PLOT_BATTERIES;
            }
            if (goodsLost == 0) {
                defeatedTruckerControllers.removeFirst();
            }else {
                if (winnerTruckerController !=null) {
                    cardState = CardState.PLOT_GOOD;
                }else cardState=CardState.ESECUZIONE;
            }
        }
        return cardState;

    }

    private void removeWithoutAsking(TruckerController truckerController, ArrayList<Goods> goodsList, Bank bank) throws GeneralError{
        if(this.numOfCurrTypeOfGood == 0){
            throw new GeneralError("Remove without asking: NumOfGoods is 0 but you called removeWithoutAsking");
        }
        if(goodsList == null){
            throw new GeneralError("Remove without asking: goodsList is null");
        }
        ArrayList<Goods> list = new ArrayList<>();
        for (int i = 0; i < numOfCurrTypeOfGood; i++) {
            Goods good = goodsList.getFirst();
            list.add(good);
            bank.addGoodsInBank(good.getValue());
            goodsList.removeFirst();
            goodsLost--;
        }
        truckerController.getSpaceShip().removeGoods(list);

    }

    public ArrayList<Goods> plotGoodsList(TruckerController truckerController) throws GeneralError {
        if (cardState != CardState.PLOT_GOODSLIST){
            throw new GeneralError("plotGoodsList not callable in the state " + cardState);
        }
        if(truckerController == null){
            throw new GeneralError("trucker is null");
        }
        if(!goodsToRemove.isEmpty()){
            throw new GeneralError("Tried to set a new goodsToRemove list but it isn't empty");
        }
        ArrayList<Goods> goodsList = truckerController.getSpaceShip().getGoodsList();
        for (int i = 0; i < numOfCurrTypeOfGood; i++) {
            goodsToRemove.add(goodsList.getFirst());
        }
        cardState = CardState.REMOVE_ASKING;
        return goodsToRemove;
    }

    /**
     *
     * @param indexes: contains the indexes of the Goods within the list displayed on the screen that the player wants to remove from his ship
     * @param truckerController
     */

    public CardState removeAsking(ArrayList<Integer> indexes, TruckerController truckerController, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen, GeneralError {
        if (cardState != CardState.REMOVE_ASKING){
            throw new GeneralError("Can't call removeAsking in the state " + cardState);
        }
        if(indexes.size() == goodsLost){
            throw new NotEnoughGoodsChosen("The number of goods selected is not " + goodsLost);
        }
        for(Integer index : indexes){
            if(index < 0 || index >= goodsToRemove.size()){
                throw new InvalidGoodChosen("A good with an index out of range has been chosen");
            }
        }
        if(truckerController == null){
            throw new GeneralError("trucker is null");
        }
        goodsLost = 0;
        ArrayList<Goods> list = new ArrayList<>();
        for(Integer index : indexes){
            list.add(goodsToRemove.get(index));
        }
        removeWithoutAsking(truckerController,list,bank);
        cardState = CardState.REMOVE_GOODS;
        return cardState;
    }

    public int getNumOfBatteriesToRemove() throws GeneralError{
        if(cardState != CardState.PLOT_BATTERIES){
            throw new GeneralError("Can't call getNumOfBatteriesToRemove in the state " + cardState);
        }
        return goodsLost;
    }

    public CardState removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries, TruckerController truckerController) throws GeneralError,InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries {
        if(cardState!= CardState.REMOVE_BATTERIES){
            throw new GeneralError("Can't call removeBatteries in the state " + cardState);
        }
        if(truckerController == null){
            throw new GeneralError("trucker is null");
        }
        if(!truckerController.getSpaceShip().checkBatteryStorage(coordinatesToRemoveBatteries)){
            throw new InvalidCoordinates("Not all components selected are Battery Storages");
        }
        int counterOfBatteriesSelectedByPlayer = 0;
        for(int i = 0; i < coordinatesToRemoveBatteries.size(); i++){
            counterOfBatteriesSelectedByPlayer += coordinatesToRemoveBatteries.get(i)[2];
        }
        if(counterOfBatteriesSelectedByPlayer != goodsLost){
            throw new WrongNumberOfBatteriesSelected("The total number of batteries you selected to remove is not " + goodsLost);
        }
        try{
            truckerController.getSpaceShip().removeBatteries(coordinatesToRemoveBatteries);
        }catch(NotEnoughBatteries e){
            throw new NotEnoughBatteries("Not enough batteries");
        }
        cardState = CardState.REMOVE_GOODS;
        return cardState;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public CardState cardEffect(Game game) throws GeneralError{
        if(cardState != CardState.ESECUZIONE){
            throw new GeneralError("Can't call cardEffect in the state " + cardState);
        }
        if(chooseToGain){
            game.moveBackward(winnerTruckerController, days);
        }
        cardState = CardState.EXIT;
        return cardState;
    }
}
