package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.utilities.exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


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

    private CardState cardState;
    private int indexOfCurrentTrucker;
    private Trucker winnerTrucker; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private ArrayList<Trucker> defeatedTruckers;
    private boolean winDecision;

    private int goodsLostCount;
    private int currGoodIndex;

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
        this.cardState = CardState.ATTESA_RISPOSTE;
        this.indexOfCurrentTrucker = 0;
        this.winnerTrucker =null;
        this.winDecision=false;
        this.currGoodIndex = 0;
        this.goodsLostCount = goodsLostCount;
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
    public int getGoodsLostCount() {
        return goodsLostCount;
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


    public CardState getCardState() {
        return cardState;
    }

    @Override
    public ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Do you want to activate your doubleCannons?",
                days,
                -1,
                -1,
                new ArrayList<>(Arrays.asList(goodsGained)),
                null,
                smugglersPower,
                -1,
                goodsLost,
                null,
                null,
                null,
                null,
                this.getCardState()
        );
    }


    private ServerEvent fightIsWin() throws GeneralError {
        if (Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() > smugglersPower) {
            winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
            cardState = CardState.GOODSTOADD;
            return new PlotGood(true, this.cardState, winnerTrucker.getNickname(), goodsGained[currGoodIndex]);
        }else if(Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() == smugglersPower){
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()) {
                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    cardState = CardState.REMOVE_GOODS;
                    indexOfCurrentTrucker = 0;
                    return removeGoods();
                }
            }else{
                indexOfCurrentTrucker++;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, this.cardState, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }

        }else {
            defeatedTruckers.add(Game.getIstance().getTrucker(indexOfCurrentTrucker));
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()) {
                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    cardState = CardState.REMOVE_GOODS;
                    indexOfCurrentTrucker = 0;
                    return removeGoods();
                }
            }else{
                indexOfCurrentTrucker++;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, this.cardState, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }
        }
    }

    public ServerEvent activateCannons(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws GeneralError, InvalidComponentsException, NotEnoughBatteries {
        if(cardState!=CardState.ACTIVATE_CANNONS){
            throw new GeneralError("Card state is not ATTESA_RISPOSTE");
        }
        if(!activate){
            return fightIsWin();
        }else{
            if(Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getNumDoubleCannon()==0){
                return fightIsWin();
            }else{
                if (!Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().checkDoubleCannon(coord)){
                    throw new InvalidComponentsException("Not all are double cannons");
                }else if(!Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().checkBatteryStorage(batteryCoord)){
                    throw new InvalidComponentsException("Not all are batteries");
                }else
                {
                    Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().activateDoubleCannons(coord, batteryCoord);
                    return fightIsWin();
                }
            }
        }
    }


    /**
     * Sets the coordinates of the loads where the winner trucker wants to add the goods gained
     * @param goodsCoordinates list of coordinatest
     */
    @Override
    public ServerEvent setGoodsCoordinates(Boolean stop, int[] goodsCoordinates, int value) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }
        if (!winnerTrucker.getSpaceShip().checkLoad(goodsCoordinates)) { //check load deve controllare il singolo componente
            throw new InvalidCoordinates("Not goods deposit");
        }
        if(stop){
            cardState = CardState.REARRANGE;
            return new RearrangeEvent(false, this.getCardState(), this.winnerTrucker.getNickname());
        }else {
            try {
                // addGood fa anche la remove dalla bank della merce corrente
                winnerTrucker.getSpaceShip().addGood(goodsGained[currGoodIndex], goodsCoordinates, value, Game.getIstance().getBank());
            } catch (InvalidPositionException e) {
                cardState = CardState.GOODSTOADD;
                throw e;
            }
            if (currGoodIndex == goodsGained.length - 1) { // ci va il -1 qui!!!
                cardState = CardState.REARRANGE;
                return new RearrangeEvent(false, this.getCardState(), this.winnerTrucker.getNickname());
            } else {
                currGoodIndex++;
                //TODO: Check che non ci sia loop infinito
                do {
                    if (Game.getIstance().getBank().getGoodsInBank(goodsGained[currGoodIndex].getValue()) >= 1) {
                        cardState = CardState.CHOOSE_TO_GAIN;
                        return new PlotGood(false, this.getCardState(), winnerTrucker.getNickname(), goodsGained[currGoodIndex]);
                    } else {
                        currGoodIndex++;
                        if (currGoodIndex == goodsGained.length) {
                            cardState = CardState.REARRANGE;
                            return new RearrangeEvent(false, this.getCardState(), this.winnerTrucker.getNickname());
                        }
                    }
                } while (true);
            }
        }
    }

    // si potrebbe non metter contiune e procedere se tutto e a null
    @Override
    public ServerEvent rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean stop) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.REARRANGE) {
            throw new GeneralError("Card state is not REARRANGE");
        }
        if (!stop) {
            if (coord1 == null && !winnerTrucker.getSpaceShip().checkLoad(coord1) && coord2 == null && !winnerTrucker.getSpaceShip().checkLoad(coord2)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                winnerTrucker.getSpaceShip().swapGoods(coord1, value1, coord2, value2);
                return new RearrangeEvent(false, this.getCardState(), this.winnerTrucker.getNickname());
            } catch (InvalidPositionException e) {
                cardState = CardState.REARRANGE;
                throw new InvalidPositionException("invalid positions");
            }
        } else{
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }
    }

    //RIMOZIONE MERCI DEGLI SCONFITTI
    @Override
    public ServerEvent removeGoods() throws GeneralError{
        if(cardState != CardState.REMOVE_GOODS){
            throw new GeneralError("plotGoodsToRemove not callable in the state " + cardState);
        }
        ArrayList<Goods> goodsList = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getGoodsList();

        if(!defeatedTruckers.isEmpty()) {
            // se il colore corrente è minore uguale a 3 allora devo togliere le merci
            while (colorOfCurrGoodToRemove <= 4 && goodsLostCount > 0) {
                switch (colorOfCurrGoodToRemove) {
                    case 4 -> {
                        numOfCurrTypeOfGood = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getRedGoods();
                        colorOfCurrGoodToRemove--;
                    }
                    case 3 -> {
                        numOfCurrTypeOfGood = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getYellowGoods();
                        colorOfCurrGoodToRemove--;
                    }
                    case 2 -> {
                        numOfCurrTypeOfGood = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getGreenGoods();
                        colorOfCurrGoodToRemove--;
                    }
                    case 1 -> {
                        numOfCurrTypeOfGood = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getBlueGoods();
                        colorOfCurrGoodToRemove--;
                    }
                }

                if (numOfCurrTypeOfGood <= goodsLostCount) {
                    removeWithoutAsking(defeatedTruckers.get(indexOfCurrentTrucker), goodsList, Game.getIstance().getBank());
                } else {
                    cardState = CardState.PLOT_GOODSLIST;
                    ArrayList<Goods> goodsList = trucker.getSpaceShip().getGoodsList();
                    for (int i = 0; i < numOfCurrTypeOfGood; i++) {
                        goodsToRemove.add(goodsList.getFirst());
                    }
                    return new
                    //TODO: plotta la lista al corrente delle merci da cui può scegliere cosa eliminare
                    // plottare beni da eliminare;
                }
            }
            if (goodsLostCount > 0) {
                // se il colore corrente è maggiore di 3 allora vuol dire che devo togliere batterie se ho ancora merci da rimuovere
                cardState = CardState.PLOT_BATTERIES;
                return new //plottare lista batterie;
            }
            if (goodsLostCount == 0) {
                defeatedTruckers.removeFirst();
            }
        }
    }

    private ServerEvent removeWithoutAsking(ArrayList<Goods> goodsList) throws GeneralError{
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
            Game.getIstance().getBank().addGoodsInBank(good.getValue());
            goodsList.removeFirst();
            goodsLostCount--;
        }
        defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().removeGoods(list);

        //TODO: plot della remove list a quello dopo se questo non deve togliere batterie (se questo era l'ultimo chiama cardEffect)
        //TODO: plot di quante batterie rimuovere a quello corrente se non aveva abbastanza merci

    }


    /**
     *
     * @param indexes: contains the indexes of the Goods within the list displayed on the screen that the player wants to remove from his ship
     */
    @Override
    public ServerEvent removeAsking(ArrayList<Integer> indexes) throws InvalidGoodChosen, NotEnoughGoodsChosen, GeneralError {
        if (cardState != CardState.REMOVE_ASKING){
            throw new GeneralError("Can't call removeAsking in the state " + cardState);
        }
        if(indexes.size() == goodsLostCount){
            throw new NotEnoughGoodsChosen("The number of goods selected is not " + goodsLostCount);
        }
        for(Integer index : indexes){
            if(index < 0 || index >= goodsToRemove.size()){
                throw new InvalidGoodChosen("A good with an index out of range has been chosen");
            }
        }
        goodsLostCount = 0;
        ArrayList<Goods> list = new ArrayList<>();
        for(Integer index : indexes){
            list.add(goodsToRemove.get(index));
        }
        removeWithoutAsking(defeatedTruckers.get(indexOfCurrentTrucker),list,Game.getIstance().getBank());
        cardState = CardState.REMOVE_GOODS;
        return removeWithoutAsking(list);
    }

    @Override
    public ServerEvent removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws GeneralError,InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries {
        if(cardState!= CardState.REMOVE_BATTERIES){
            throw new GeneralError("Can't call removeBatteries in the state " + cardState);
        }
        if(trucker == null){
            throw new GeneralError("trucker is null");
        }
        if(!trucker.getSpaceShip().checkBatteryStorage(coordinatesToRemoveBatteries)){
            throw new InvalidCoordinates("Not all components selected are Battery Storages");
        }
        int counterOfBatteriesSelectedByPlayer = 0;
        for(int i = 0; i < coordinatesToRemoveBatteries.size(); i++){
            counterOfBatteriesSelectedByPlayer += coordinatesToRemoveBatteries.get(i)[2];
        }
        if(counterOfBatteriesSelectedByPlayer != goodsLostCount){
            throw new WrongNumberOfBatteriesSelected("The total number of batteries you selected to remove is not " + goodsLostCount);
        }
        try{
            trucker.getSpaceShip().removeBatteries(coordinatesToRemoveBatteries);
        }catch(NotEnoughBatteries e){
            throw new NotEnoughBatteries("Not enough batteries");
        }
        cardState = CardState.REMOVE_GOODS;
        return new //conferma rimozione batterie;
    }


    /**
     * Applies the changes provided by the card
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError{
        if(cardState != CardState.ESECUZIONE){
            throw new GeneralError("Can't call cardEffect in the state " + cardState);
        }
        if(winDecision){
            Game.getIstance().moveBackward(winnerTrucker, days);
        }
        cardState = CardState.EXIT;
        return new CardFinished(true, this.cardState, Game.getIstance().getTruckerList());
    }
}
