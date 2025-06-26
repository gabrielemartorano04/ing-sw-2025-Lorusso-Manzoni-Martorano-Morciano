package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * smugglers cannon power
     */
    private final int smugglersPower;

    /**
     * number of goods to remove from losers' ships
     */
    private final int goodsLost;

    /**
     * flight days to lose
     */
    private final int days;

    /**
     * goods that can be obtained by the winner
     */
    private final Goods[] goodsGained;

    private CardState cardState;

    /**
     * index of the current player to handle the fact that clients have to respect a specific turn
     */
    private int indexOfCurrentTrucker;

    /**
     * player that has defeated the smugglers
     */
    private Trucker winnerTrucker;

    /**
     * list of all defeated players
     */
    private final ArrayList<Trucker> defeatedTruckers;

    /**
     * decision taken by the potential winner if there is one
     */
    private boolean winDecision;

    /**
     * number of goods to lose by the current player
     */
    private int goodsToLoseCurrTrucker;

    /**
     * index of the current good
     */
    private int currGoodIndex;

    /**
     * color of the current goods to remove
     * 4 = red
     * 3 = yellow
     * 2 = green
     * 1 = blue
     */
    private int colorOfCurrGoodToRemove;

    /**
     * number of goods of the current color in the ship of the current player
     */
    private int numGoodCurrType;

    /**
     * list of goods to be removed for the current player if he can have the decision on which to remove
     */
    private final ArrayList<Goods> goodsToRemove;


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
    public Smugglers(boolean trialFlight, int smugglersPower, int goodsLost, int days, Goods[] goodsGained, int level, String frontImage, String backImage, int id) {
        super("Smugglers", "Illegal traders of goods", trialFlight, level, frontImage, backImage, id);
        this.smugglersPower = smugglersPower;
        this.goodsLost = goodsLost;
        this.days = days;
        this.goodsGained = goodsGained;
        this.cardState = CardState.ACTIVATE_CANNONS;
        this.indexOfCurrentTrucker = 0;
        this.winnerTrucker =null;
        this.winDecision=false;
        this.currGoodIndex = 0;
        this.colorOfCurrGoodToRemove = 4;
        this.numGoodCurrType = 0;
        this.goodsToRemove = new ArrayList<>();
        this.defeatedTruckers = new ArrayList<>();
        this.goodsToLoseCurrTrucker = goodsLost;
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

    /**
     * @return an event containing all the card details
     */
    @Override
    public synchronized ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Choose if you want to activate the double cannons. If yes type the coordinates of the cannons then 'battery' and battery storage(s) coordinates . Type 'y, <x1> <y1>, <x2> <y2>,..., battery, <xs1> <ys1> - <numofbatteries>,...' or 'n': ",
                days,
                -1,
                -1,
                Arrays.asList(goodsGained),
                null,
                smugglersPower,
                -1,
                goodsLost,
                null,
                null,
                null,
                null,
                ParserState.ACTIVATE_CANNONS,
                null,
                Game.getIstance().getTrack(),
                this
        );
    }

    /**
     * handle the current fight between pirates and a player
     *
     * @return serverEvent to send to clients
     */
    private ServerEvent fightIsWin() throws GeneralError {
        float power = Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower();

        if (power > smugglersPower) {
            return handleVictory();
        }else if(power == smugglersPower){
            return handleDraw();
        }else {
            return handleDefeat();
        }
    }

    /**
     * handle the victory of the fight
     *
     * @return ServerEvent to send to clients
     */
    private ServerEvent handleVictory() {
        winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
        cardState = CardState.GOODSTOADD;
        return new PlotGood(true, winnerTrucker.getNickname(),winnerTrucker.getSpaceShip(),goodsGained[currGoodIndex], Game.getIstance().getTruckerList());
    }

    /**
     * handle draw case
     *
     * @return ServerEvent to send to clients
     */
    private ServerEvent handleDraw() throws GeneralError {
        if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
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
            return new BooleanDecision(true, ParserState.ACTIVATE_CANNONS, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), Game.getIstance().getTruckerList());
        }
    }

    /**
     * handle the defeat of the current player
     *
     * @return ServerEvent to send to clients
     */
    private ServerEvent handleDefeat() throws GeneralError {
        defeatedTruckers.add(Game.getIstance().getTrucker(indexOfCurrentTrucker));
        if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
            cardState = CardState.REMOVE_GOODS;
            indexOfCurrentTrucker = 0;
            return removeGoods();
        }else{
            indexOfCurrentTrucker++;
            return new BooleanDecision(true, ParserState.ACTIVATE_CANNONS, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), Game.getIstance().getTruckerList());
        }
    }


    /**
     * Sets for each trucker the coordinates of the double cannons that he/she wants to activate
     *
     * @param activate: true if the current player wants to activate any double Cannons, false otherwise
     * @param coord: coordinates of the doubleCannons to activate
     * @param batteryCoord: coordinates of the batteryStorages from which remove the batteries
     * @return serverEvent to send to clients
     * @throws InvalidComponentsException coordinates do not match to their component type
     * @throws NotEnoughBatteries not enoughBatteries indicated to activate the doubleCannons selected
     */
    @Override
    public synchronized ServerEvent activateCannons(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws GeneralError, InvalidComponentsException, NotEnoughBatteries {
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
     *
     * Adds the current good to the current trucker spaceship or handles the evolution of the card
     *
     * @param answer true if the current player wants this good false otherwise
     * @param goodsCoordinates list of coordinatest
     * @param positionInLoad position in the load to insert the good
     * @return serverEvent to send to clients
     * @throws InvalidCoordinates coordinates are not of a load
     * @throws InvalidPositionException not a special load but that good was special
     */
    @Override
    public synchronized ServerEvent addGood(Boolean answer, int[] goodsCoordinates, int positionInLoad) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }

        if(!answer){
            return handleGoodRefused();
        }else {
            handleGoodAccepted(goodsCoordinates, positionInLoad);

            if (currGoodIndex == goodsGained.length - 1) {
                cardState = CardState.REARRANGE;
                return new RearrangeEvent(true, this.winnerTrucker.getNickname(), Game.getIstance().getTruckerList());
            } else {
                currGoodIndex++;
                return proceedToNextAvailableGoodOrRearrange();
            }
        }
    }

    /**
     * handle the fact that the current player doesn't want the current good
     *
     * @return serverEvent to send to clients
     */
    private ServerEvent handleGoodRefused() {
        if (currGoodIndex == goodsGained.length - 1) {
            cardState = CardState.REARRANGE;
            return new RearrangeEvent(true, winnerTrucker.getNickname(), Game.getIstance().getTruckerList());
        }

        currGoodIndex++;
        return proceedToNextAvailableGoodOrRearrange();
    }

    /**
     * proceeds to the next available Good
     *
     * @return serverEvent to send to clients
     */
    private ServerEvent proceedToNextAvailableGoodOrRearrange() {
        while (currGoodIndex < goodsGained.length) {
            Goods g = goodsGained[currGoodIndex];
            if (Game.getIstance().getBank().getGoodsInBank(g.getValue()) >= 1) {
                cardState = CardState.GOODSTOADD;
                return new PlotGood(true, winnerTrucker.getNickname(), winnerTrucker.getSpaceShip(), g, Game.getIstance().getTruckerList());
            }
            currGoodIndex++;
        }

        cardState = CardState.REARRANGE;
        return new RearrangeEvent(true, winnerTrucker.getNickname(), Game.getIstance().getTruckerList());
    }

    /**
     * tries to add the corrent good to the ship in the coordinates indicated
     */
    private void handleGoodAccepted(int[] goodsCoordinates, int positionInLoad) throws InvalidCoordinates, InvalidPositionException {

        if (!winnerTrucker.getSpaceShip().checkLoad(goodsCoordinates)) {
            throw new InvalidCoordinates("Not goods deposit");
        }
        Goods goodToAdd = goodsGained[currGoodIndex];
        goodToAdd.setX(goodsCoordinates[0]);
        goodToAdd.setY(goodsCoordinates[1]);

        try {
            winnerTrucker.getSpaceShip().addGood(goodToAdd, goodsCoordinates, positionInLoad, Game.getIstance().getBank());
        } catch (InvalidPositionException e) {
            cardState = CardState.GOODSTOADD;
            throw e;
        }
    }


    /**
     * switch 2 goods to rearrange them in the ship
     * @param coord1 initial position
     * @param coord2 final position
     * @param value1 initial internal position
     * @param value2 final internat position
     * @param stop when to end the swaps
     * @return serverEvent to send to clients
     * @throws InvalidCoordinates coordinates are not valid
     * @throws InvalidPositionException tried to put a special good in a not special load
     */
    @Override
    public synchronized ServerEvent rearrange(int[] coord1, int[] coord2, int value1, int value2, boolean stop) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.REARRANGE) {
            throw new GeneralError("Card state is not REARRANGE");
        }
        if (!stop) {
            if (coord1 == null && !winnerTrucker.getSpaceShip().checkLoad(coord1) && coord2 == null && !winnerTrucker.getSpaceShip().checkLoad(coord2)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                winnerTrucker.getSpaceShip().swapGoods(coord1, value1, coord2, value2);
                return new RearrangeEvent(true, this.winnerTrucker.getNickname(), Game.getIstance().getTruckerList());
            } catch (InvalidPositionException e) {
                cardState = CardState.REARRANGE;
                throw new InvalidPositionException("invalid positions");
            }
        } else{
            if(defeatedTruckers.isEmpty()){
                cardState = CardState.ESECUZIONE;
                return cardEffect();
            }else{
                indexOfCurrentTrucker = 0;
                cardState = CardState.REMOVE_GOODS;
                return removeGoods();
            }
        }
    }


    /**
     * handle the remotion of goods from the ship of the current player
     *
     * @return serverEvent to send to clients
     */
    @Override
    public synchronized ServerEvent removeGoods() throws GeneralError {
        if (cardState != CardState.REMOVE_GOODS) {
            throw new GeneralError("plotGoodsToRemove not callable in the state " + cardState);
        }

        if (defeatedTruckers.isEmpty()) {
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }

        SpaceShip ship = defeatedTruckers.getFirst().getSpaceShip();
        String nickname = defeatedTruckers.getFirst().getNickname();
        ArrayList<Goods> goodsList = ship.getGoodsList();

        while (colorOfCurrGoodToRemove > 0 && goodsToLoseCurrTrucker > 0) {
            numGoodCurrType = getNumGoodsForColor(ship, colorOfCurrGoodToRemove);
            System.out.println(nickname);
            System.out.println("sto cercando " + colorOfCurrGoodToRemove);
            System.out.println("numGoodCurrType: " + numGoodCurrType + " numGoodCurrType: " + goodsToLoseCurrTrucker);
            if (numGoodCurrType > 0 && numGoodCurrType <= goodsToLoseCurrTrucker) {
                return removeWithoutDecision(goodsList);
            }
            else if (numGoodCurrType > 0) {
                cardState = CardState.REMOVE_ASKING;
                goodsToRemove.clear();
                for (Goods g : goodsList) {
                    if (g.getValue() == colorOfCurrGoodToRemove && goodsToRemove.size() < numGoodCurrType) {
                        goodsToRemove.add(g);
                    }
                }
                return new RemoveGoods(true, nickname, ship, goodsToRemove, Game.getIstance().getTruckerList());
            }
            colorOfCurrGoodToRemove--;
        }

        if (colorOfCurrGoodToRemove == 0 && goodsToLoseCurrTrucker > 0) {
            return handleBatteryOrNextTrucker(ship, nickname);
        }

        throw new GeneralError("RemoveGoods not working correctly");
    }


    /**
     * handle the remotion of batteries if necessary or the transition to the next player
     *
     * @param ship of the current player
     * @param nickname of the current player
     * @return serverEvent to send to clients
     */
    private ServerEvent handleBatteryOrNextTrucker(SpaceShip ship, String nickname) throws GeneralError {
        if (ship.getNumBatteries() == 0) {
            defeatedTruckers.removeFirst();
            goodsToLoseCurrTrucker = goodsLost;
            if (defeatedTruckers.isEmpty()) {
                cardState = CardState.ESECUZIONE;
                return cardEffect();
            } else {
                System.out.println("Remotion Confirmed in handleBatteryOrNextTrucker");
                return new RemotionConfirmed(ParserState.REMOTION_CONFIRMED, nickname, ship);
            }
        } else {
            cardState = CardState.REMOVE_BATTERIES;
            return new RemoveBatteries(true, nickname, ship, goodsToLoseCurrTrucker, Game.getIstance().getTruckerList());
        }
    }


    /**
     * @param ship of the current player
     * @param colorCode current good's color
     * @return number of goods of color "colorCode" in the current player's ship
     */
    private int getNumGoodsForColor(SpaceShip ship, int colorCode) {
        return switch (colorCode) {
            case 4 -> ship.getRedGoods();
            case 3 -> ship.getYellowGoods();
            case 2 -> ship.getGreenGoods();
            case 1 -> ship.getBlueGoods();
            default -> 0;
        };
    }

    /**
     * removes all the goods in the list from the ship of the current player
     *
     * @param goodsListFromShip goods to remove
     * @return serverEvent to send to clients
     */
    private ServerEvent removeWithoutDecision(ArrayList<Goods> goodsListFromShip) throws GeneralError {
        if (goodsListFromShip == null) {
            throw new GeneralError("Remove without asking: goodsList is null");
        }
        if(goodsListFromShip.isEmpty()){
            System.out.println("Remove without asking: goodsList is empty");
        }

        ArrayList<Goods> list = new ArrayList<>();
        for (int i = 0; i < numGoodCurrType; i++) {
            Goods good = goodsListFromShip.get(i);
            list.add(good);
            Game.getIstance().getBank().addGoodsInBank(good.getValue());
            goodsToLoseCurrTrucker--;
        }

        defeatedTruckers.getFirst().getSpaceShip().removeGoods(list);

        SpaceShip spaceShip = defeatedTruckers.getFirst().getSpaceShip();
        String nickname = defeatedTruckers.getFirst().getNickname();

        if (goodsToLoseCurrTrucker == 0 || (goodsToLoseCurrTrucker > 0 && spaceShip.getNumBatteries() == 0)) {
            defeatedTruckers.removeFirst();
            goodsToLoseCurrTrucker = goodsLost;
            cardState = CardState.REMOVE_GOODS;
            System.out.println("Remotion Confirmed in remove without decision");
            return new RemotionConfirmed(ParserState.REMOTION_CONFIRMED,nickname,spaceShip);

        } else if((goodsToLoseCurrTrucker > 0 && !spaceShip.getGoodsList().isEmpty())){
            cardState = CardState.REMOVE_GOODS;
            return removeGoods();

        }
        else {
            cardState = CardState.REMOVE_BATTERIES;
            return new RemoveBatteries(true, nickname,spaceShip, goodsToLoseCurrTrucker, Game.getIstance().getTruckerList());
        }
    }


    /**
     *  removes all the goods from the goodsToRemove list based on the indexes chosen by the player
     *
     * @param indexesToRemove: contains the indexes of the goods that the player wants to remove
     * @return serverEvent to send to clients
     */

    @Override
    public synchronized ServerEvent removeGoodsDecided(ArrayList<Integer> indexesToRemove) throws InvalidGoodChosen, NotEnoughGoodsChosen, GeneralError {
        if (cardState != CardState.REMOVE_ASKING){
            throw new GeneralError("Can't call removeAsking in the state " + cardState);
        }
        if(indexesToRemove.size() != goodsToLoseCurrTrucker){
            throw new NotEnoughGoodsChosen("The number of goods selected is not " + goodsToLoseCurrTrucker);
        }
        for(Integer index : indexesToRemove){
            if(index < 0 || index >= goodsToRemove.size()){
                System.out.println("goodsToRemove: " + goodsToRemove + ", index: " + index);
                throw new InvalidGoodChosen("A good with an index out of range has been chosen");
            }
        }
        goodsToLoseCurrTrucker = 0;
        ArrayList<Goods> selectedGoods = new ArrayList<>();
        for (int i : indexesToRemove) {
            Goods g = goodsToRemove.get(i);
            selectedGoods.add(g);
            Game.getIstance().getBank().addGoodsInBank(g.getValue());
        }

        goodsToRemove.clear();

        SpaceShip ship = defeatedTruckers.getFirst().getSpaceShip();
        String nickname = defeatedTruckers.getFirst().getNickname();

        ship.removeGoods(selectedGoods);

        defeatedTruckers.removeFirst();
        goodsToLoseCurrTrucker = goodsLost;
        cardState = CardState.REMOVE_GOODS;
        System.out.println("RemotionConfirmed in removeGoodsDecided");
        return new RemotionConfirmed(ParserState.REMOTION_CONFIRMED,nickname,ship);
    }

    /**
     *  removes batteries from the ship of the current player
     *
     * @param coordinatesToRemoveBatteries: coordinates of the battery storages from which remove the number of batteries indicated
     * @return serverEvent to send to clients
     */
    @Override
    public synchronized ServerEvent removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries) throws GeneralError,InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries {
        if(cardState!= CardState.REMOVE_BATTERIES){
            throw new GeneralError("Can't call removeBatteries in the state " + cardState);
        }
        if(!defeatedTruckers.getFirst().getSpaceShip().checkBatteryStorage(coordinatesToRemoveBatteries)){
            throw new InvalidCoordinates("Not all components selected are Battery Storages");
        }
        int counterOfBatteriesSelectedByPlayer = 0;
        for (int[] coordinatesToRemoveBattery : coordinatesToRemoveBatteries) {
            counterOfBatteriesSelectedByPlayer += coordinatesToRemoveBattery[2];
        }
        if(counterOfBatteriesSelectedByPlayer != goodsToLoseCurrTrucker){
            throw new WrongNumberOfBatteriesSelected("The total number of batteries you selected to remove is not " + goodsToLoseCurrTrucker);
        }
        try{
            defeatedTruckers.getFirst().getSpaceShip().removeBatteries(coordinatesToRemoveBatteries);
        }catch(NotEnoughBatteries e){
            throw new NotEnoughBatteries("There is a battery storage that has less batteries then the number of batteries you wanted to remove in that one");
        }

        SpaceShip spaceShip = defeatedTruckers.getFirst().getSpaceShip();
        String nickname = defeatedTruckers.getFirst().getNickname();

        defeatedTruckers.removeFirst();
        goodsToLoseCurrTrucker = goodsLost;
        cardState = CardState.REMOVE_GOODS;
        System.out.println("Remotion Confirm removeBatteries");
        return new RemotionConfirmed(ParserState.REMOTION_CONFIRMED,nickname,spaceShip);
    }


    /**
     * deactivate every type of component from every player's ship
     */
    private void deactivateTruckersSpaceShip(){
        for(Trucker trucker: Game.getIstance().getTruckerList()){
            trucker.getSpaceShip().deactivateDoubleCannons();
            trucker.getSpaceShip().deactivateDoubleEngines();
            trucker.getSpaceShip().deActivateShields();
        }
    }

    /**
     * Applies the changes provided by the card
     *
     * @return serverEvent to send to clients
     */
    @Override
    public synchronized ServerEvent cardEffect() throws GeneralError{
        if(cardState != CardState.ESECUZIONE){
            throw new GeneralError("Can't call cardEffect in the state " + cardState);
        }
        if(winDecision && winnerTrucker!=null){
            winDecision = false;
            Game.getIstance().moveBackward(winnerTrucker, days);
        }
        ArrayList<Trucker> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();

        deactivateTruckersSpaceShip();

        GamePersistence.saveGame(GameController.getIstance());
        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, Game.getIstance().getTrack());
    }

    public int getIndexOfCurrentTrucker() {
        return indexOfCurrentTrucker;
    }

    public int getPower() {
        return smugglersPower;
    }

    public void setCardState(CardState cardState) {
        this.cardState = cardState;
    }

    public void addDefeatedTruckers(Trucker defeatedTruckers) {
        this.defeatedTruckers.add(defeatedTruckers);
    }

    public void addGoodToRemove(Goods good){
        goodsToRemove.add(good);
    }

    public void setGoodsToLoseCurrTrucker(int goodsToLoseCurrTrucker) {
        this.goodsToLoseCurrTrucker = goodsToLoseCurrTrucker;
    }

    public ArrayList<Goods> getGoodsToRemove() {
        return goodsToRemove;
    }

    public void setIndexOfCurrentTrucker(int indexOfCurrentTrucker) {
        this.indexOfCurrentTrucker = indexOfCurrentTrucker;
    }

    public void setWinnerTrucker(Trucker winnerTrucker) {
        this.winnerTrucker = winnerTrucker;
    }
}
