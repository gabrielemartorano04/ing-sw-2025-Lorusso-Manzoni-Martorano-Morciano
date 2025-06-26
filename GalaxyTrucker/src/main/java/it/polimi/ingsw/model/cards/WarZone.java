package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 *
 */
public class WarZone extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * current state of the card, helper for debug and checking consistency
     */
    private CardState state;

    /**
     * index of the current condition
     */
    private int currConditionIndex;

    /**
     * list of conditions to be examinated ordered by the game rules
     */
    private final ArrayList<Condition> conditionsOrder;

    /**
     * map of penalties associated to their condition
     */
    private final Map<Condition, Effect> penalties;

    /**
     * A list of shots for this specific Pirates card.
     * Each element represents a shot, where:
     * - index 0 indicates the shot size (1 = large, 0 = small)
     * - index 1 indicates the direction from which the shot comes
     *   (0 = top, 1 = right, 2 = bottom, 3 = left)
     */
    private final ArrayList<int[]> shoots;

    /**
     * current condition in examination
     */
    private Condition currCondition;

    /**
     * weakest trucker based on the current condition
     */
    private Trucker weakestTrucker;

    /**
     * roll dices result for the current shot
     */
    private int roll;

    /**
     * index of the current player to handle the fact that clients have to respect a specific turn
     */
    private int indexOfCurrTrucker;

    /**
     * index of the current shot
     */
    private int currShootIndex;

    /**
     * current shot
     */
    private int[] shoot;

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
     * list of goods to be removed for the player if he can have the decision on which to remove
     */
    private final ArrayList<Goods> goodsToRemove;

    /**
     * number of goods to remove from the weakest player ship if the penalty is to lose goods
     */
    private int goodsToLose;

    /**
     * boolean value to initialize the goodsToLose value if the penalty is to lose goods
     */
    private boolean initializeRemoveGoods;

    /**
     * number of crew members to lose if the penalty is to lose humans
     */
    private int numOfHumanToRemove;


    public WarZone(boolean trialFlight, int level, ArrayList<Condition> conditionsOrder, Map<Condition, Effect> penalties, ArrayList<int[]> shoots , String frontImage, String backImage, int id) {
        super("War Zone", "A dangerous conflict zone", trialFlight, level, frontImage, backImage, id);
        this.conditionsOrder = conditionsOrder;
        this.penalties = penalties;
        this.shoots = shoots;
        this.state = CardState.WARZONE;
        this.currConditionIndex = 0;
        this.weakestTrucker = null;
        this.indexOfCurrTrucker = 0;
        this.colorOfCurrGoodToRemove = 4;
        this.numGoodCurrType = 0;
        this.goodsToLose = 0;
        this.currShootIndex = 0;
        this.currCondition = null;
        this.numOfHumanToRemove = 0;

        this.initializeRemoveGoods = true;
        this.goodsToRemove = new ArrayList<>();

    }

    public CardState getCardState() {
        return state;
    }

//    public ArrayList<Condition> getConditionsOrder() {
//        return conditionsOrder;
//    }

//    public Map<Condition, Effect> getPenalties() {
//        return penalties;
//    }

//    public ArrayList<int[]> getShoots(){
//        return shoots;
//    }

    /**
     * @return an event containing all the card details
     */
    @Override
    public ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Type 'warzone' to start WarZone card",
                -1,
                -1,
                -1,
                null,
                null,
                -1,
                -1,
                -1,
                shoots,
                null,
                this.conditionsOrder,
                this.penalties,
                ParserState.WARZONE,
                null,
                Game.getIstance().getTrack(),
                this
        );
    }


    /**
     * handle the current condition of the card
     *
     * @return serverEvent to send to clients
     */
    @Override
    public ServerEvent handleCurrentCondition() throws GeneralError{
        if(state != CardState.WARZONE){
            throw new GeneralError("CombatZone not callable in the state " + state);
        }
        if(Game.getIstance().getTruckerList().size() == 1){
            ArrayList<Trucker> truckerRemoved;
            truckerRemoved = Game.getIstance().endTurn();
            Map<String, Integer> track = new HashMap<>();
            for (Trucker t : Game.getIstance().getTruckerList()) {
                track.put(t.getNickname(), t.getPosition());
            }
            return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track);
        }
        if(currConditionIndex == conditionsOrder.size()){
            throw new GeneralError("All conditions have been verified");
        }
        indexOfCurrTrucker = 0;
        if(currConditionIndex == 0){
            this.currCondition = conditionsOrder.get(currConditionIndex);
        }
        switch(currCondition){
            case HUMANS -> {
                state = CardState.FIND_WEAKEST;
                return findWeakestTrucker();
            }
            case CANNONS -> {
                state = CardState.ACTIVATE_CANNONS;
                return new BooleanDecision(true, ParserState.ACTIVATE_CANNONS, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), Game.getIstance().getTruckerList());
            }
            case ENGINES -> {
                state = CardState.ACTIVATE_ENGINES;
                return new BooleanDecision(true, ParserState.ACTIVATE_ENGINES, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), Game.getIstance().getTruckerList());
            }
            default ->
                throw new GeneralError("Unknown condition " + currCondition);

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
    public ServerEvent activateCannons(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws GeneralError, InvalidComponentsException, NotEnoughBatteries {
        if (this.state != CardState.ACTIVATE_CANNONS) {
            throw new GeneralError("Non è possibile inserire le informazioni nello stato " + this.state);
        }
        if(activate){
            if(!Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().checkBatteryStorage(batteryCoord)){
                throw new InvalidComponentsException("Not all coordinates are battery storage");
            }
            if(!Game.getIstance().getTruckerList().get(indexOfCurrTrucker).getSpaceShip().checkDoubleCannon(coord)){
                if(Game.getIstance().getTruckerList().get(indexOfCurrTrucker).getSpaceShip().getNumDoubleCannon() == 0 ){
                    indexOfCurrTrucker++;
                    throw new InvalidComponentsException("You don't have any double engines");

                }
                throw new InvalidComponentsException("Not all components are DoubleEngine");
            }

            Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().activateDoubleCannons(coord, batteryCoord);

        }

        if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()-1) {
            this.state = CardState.FIND_WEAKEST;
            indexOfCurrTrucker=0;
            return findWeakestTrucker();
        }
        indexOfCurrTrucker++;
        return new BooleanDecision(true, ParserState.ACTIVATE_CANNONS, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(),Game.getIstance().getTruckerList());
    }


    /**
     * Sets for each trucker the coordinates of the double engines that he/she wants to activate
     *
     * @param activate: decision taken by the current player
     * @param coord: coordinates of the doubleEngines to activate
     * @param batteryCoord: coordinates of the BatteryStorages from which remove the batteries
     * @return serverEvent to send to clients
     * @throws InvalidCoordinates: coordinates are not of shields or batteryStorages
     * @throws NotEnoughBatteries: not enoughBatteries indicated
     */
    @Override
    public ServerEvent activateEngines(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws InvalidCoordinates, GeneralError, NotEnoughBatteries {
        if (this.state != CardState.ACTIVATE_ENGINES) {
            throw new GeneralError("Non è possibile inserire le informazioni nello stato " + this.state);
        }
        if(activate){
            if(!Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().checkBatteryStorage(batteryCoord)){
                throw new InvalidCoordinates("Not all coordinates are battery storage");
            }
            if(!Game.getIstance().getTruckerList().get(indexOfCurrTrucker).getSpaceShip().checkDoubleEngine(coord)){
                if(Game.getIstance().getTruckerList().get(indexOfCurrTrucker).getSpaceShip().getNumDoubleEngine() == 0 ){
                    indexOfCurrTrucker++;
                    throw new InvalidCoordinates("You don't have any double engines");

                }
                throw new InvalidCoordinates("Not all components are DoubleEngine");
            }

            Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().activateDoubleEngines(coord, batteryCoord);

        }

        if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()-1) {
            this.state = CardState.FIND_WEAKEST;
            return findWeakestTrucker();
        }
        indexOfCurrTrucker++;
        return new BooleanDecision(true, ParserState.ACTIVATE_ENGINES, Game.getIstance().getTruckerList().get(indexOfCurrTrucker).getNickname(), Game.getIstance().getTruckerList());
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
     * handle the remotion of goods from the ship of the current player
     *
     * @return serverEvent to send to clients
     */
    @Override
    public ServerEvent removeGoods() throws GeneralError{
        if(state != CardState.REMOVE_GOODS){
            throw new GeneralError("removeGoods not callable in the state " + state);
        }

        if(weakestTrucker == null){
            throw new GeneralError("removeGoods error: weakest trucker is null");
        }

        ArrayList<Goods> goodsList;

        goodsList = weakestTrucker.getSpaceShip().getGoodsList();

        if(penalties.get(currCondition).getType() != TypeOfEffect.LOSE_GOODS){
            throw new GeneralError("removegoods error - curr penalty is not lose goods");
        }
        if(this.initializeRemoveGoods){
            goodsToLose = penalties.get(currCondition).getValue();
            this.initializeRemoveGoods = false;
        }

        while (colorOfCurrGoodToRemove > 0 && goodsToLose > 0) {
            numGoodCurrType = getNumGoodsForColor(weakestTrucker.getSpaceShip(), colorOfCurrGoodToRemove);

            if (numGoodCurrType > 0 && numGoodCurrType <= goodsToLose) {
                return removeWithoutDecision(goodsList);
            }else if (numGoodCurrType > 0){
                state = CardState.REMOVE_ASKING;
                ArrayList<Goods> listOfGoods = weakestTrucker.getSpaceShip().getGoodsList();
                for (int i = 0; i < numGoodCurrType; i++) {
                    goodsToRemove.add(listOfGoods.getFirst());
                }
                return new RemoveGoods(true, weakestTrucker.getNickname(), weakestTrucker.getSpaceShip(),goodsToRemove, Game.getIstance().getTruckerList());
            }
            colorOfCurrGoodToRemove--;
        }

        if(colorOfCurrGoodToRemove == 0 && goodsToLose > 0){
            if(weakestTrucker.getSpaceShip().getNumBatteries() == 0){
                this.state = CardState.ESECUZIONE;
                return cardEffect();
            }else{
                this.state = CardState.REMOVE_BATTERIES;
                return new RemoveBatteries(true, weakestTrucker.getNickname(),weakestTrucker.getSpaceShip(), goodsToLose, Game.getIstance().getTruckerList());
            }
        }else{
            throw new GeneralError("RemoveGoods not working correctly");
        }
    }


    /**
     * removes all the goods in the list from the ship of the current player
     *
     * @param goodsList goods to remove
     * @return serverEvent to send to clients
     */
    private ServerEvent removeWithoutDecision(ArrayList<Goods> goodsList) throws GeneralError {
        if (goodsList == null) {
            throw new GeneralError("Remove without asking: goodsList is null");
        }
        ArrayList<Goods> list = new ArrayList<>();

        for (int i = 0; i < numGoodCurrType; i++) {
            Goods good = goodsList.get(i);
            list.add(good);
            Game.getIstance().getBank().addGoodsInBank(good.getValue());
            goodsToLose--;
        }
        weakestTrucker.getSpaceShip().removeGoods(list);

        if (goodsToLose == 0) {
            state = CardState.ESECUZIONE;
            return cardEffect();
        } else if((goodsToLose > 0 && !weakestTrucker.getSpaceShip().getGoodsList().isEmpty())){
            state = CardState.REMOVE_GOODS;
            return removeGoods();
        }else if (goodsToLose > 0 && weakestTrucker.getSpaceShip().getNumBatteries() == 0){
            state = CardState.ESECUZIONE;
            return cardEffect();
        }
        else {
            state = CardState.REMOVE_BATTERIES;
            return new RemoveBatteries(true, weakestTrucker.getNickname(),weakestTrucker.getSpaceShip(), goodsToLose, Game.getIstance().getTruckerList());
        }
    }

    /**
     *  removes all the goods from the goodsToRemove list based on the indexes chosen by the player
     *
     * @param indexesToRemove: contains the indexes of the goods that the player wants to remove
     * @return serverEvent to send to clients
     */
    @Override
    public ServerEvent removeGoodsDecided(ArrayList<Integer> indexesToRemove) throws InvalidGoodChosen, NotEnoughGoodsChosen, GeneralError {
        if (state != CardState.REMOVE_ASKING){
            throw new GeneralError("Can't call removeAsking in the state " + state);
        }
        if(indexesToRemove.size() != goodsToLose){
            throw new NotEnoughGoodsChosen("The number of goods selected is not " + goodsToLose);
        }

        ArrayList<Goods> selectedGoods = new ArrayList<>();
        for (int i : indexesToRemove) {
            Goods g = goodsToRemove.get(i);
            selectedGoods.add(g);
            Game.getIstance().getBank().addGoodsInBank(g.getValue());
        }
        goodsToLose = 0;

        goodsToRemove.clear();
        weakestTrucker.getSpaceShip().removeGoods(selectedGoods);
        state = CardState.ESECUZIONE;

        return cardEffect();
    }


    /**
     *  removes batteries from the ship of the current player
     *
     * @param coordinatesToRemoveBatteries: coordinates of the battery storages from which remove the number of batteries indicated
     * @return serverEvent to send to clients
     */
    @Override
    public ServerEvent removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries) throws GeneralError,InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries {
        if(state!= CardState.REMOVE_BATTERIES){
            throw new GeneralError("Can't call removeBatteries in the state " + state);
        }
        if(!weakestTrucker.getSpaceShip().checkBatteryStorage(coordinatesToRemoveBatteries)){
            throw new InvalidCoordinates("Not all components selected are Battery Storages");
        }
        int counterOfBatteriesSelectedByPlayer = 0;
        for (int[] coordinatesToRemoveBattery : coordinatesToRemoveBatteries) {
            counterOfBatteriesSelectedByPlayer += coordinatesToRemoveBattery[2];
        }
        if(counterOfBatteriesSelectedByPlayer != goodsToLose){
            throw new WrongNumberOfBatteriesSelected("The total number of batteries you selected to remove is not " + goodsToLose);
        }
        try{
            weakestTrucker.getSpaceShip().removeBatteries(coordinatesToRemoveBatteries);
        }catch(NotEnoughBatteries e){
            throw new NotEnoughBatteries("There is a battery storage that has less batteries then the number of batteries you wanted to remove in that one");
        }
        this.state = CardState.ESECUZIONE;
        return cardEffect();
    }

    /**
     * handle remotion of humans from the current player spaceShip
     * @param coordinates list of arrays of coordinates of the cabins
     * @return serverEvent to send to clients
     */
    @Override
    public ServerEvent removeHumans(ArrayList<int[]> coordinates) throws InvalidCoordinates, GeneralError {
        if(state != CardState.HUMANSTOREMOVE) {
            throw new GeneralError("Can't call setCoordinates in the state: " + state);
        }
        if(weakestTrucker ==null){
            throw new GeneralError("Tried to set coordinates when truckerThatHasDecided is null");
        }
        try{
            weakestTrucker.getSpaceShip().checkCabin(coordinates, numOfHumanToRemove);
            weakestTrucker.getSpaceShip().removeNumHuman(coordinates,numOfHumanToRemove);
            state = CardState.ESECUZIONE;
            return cardEffect();
        }catch (InvalidCoordinates e){
            throw new InvalidCoordinates(e.getMessage());
        }
    }

    /**
     * roll the dices for the current shot
     *
     * @return the event containing the roll dice result and the current meteor
     */
    @Override
    public ServerEvent rollDice() throws GeneralError {
        if(state != CardState.ROLL_DICE) {
            throw new GeneralError("Non si può girare i dadi nello stato " + this.state);
        }
        if(currShootIndex <0 || currShootIndex >= shoots.size()){
            throw new GeneralError("Stai cercando di accedere a un meteorite che non esiste, la carta dovrebbe essere finita");
        }
        shoot = shoots.get(currShootIndex);
        this.roll = Game.getIstance().rollDice();
        if (shoot[0]==1) {
            state = CardState.ESECUZIONE;
            return cardEffect();
        }else{
            state=CardState.ACTIVATE_SHIELDS;
            return new RollDiceResult(ParserState.ACTIVATE_SHIELD, currShootIndex,shoot,null, roll, weakestTrucker.getNickname(), true, "smallmeteor", Game.getIstance().getTruckerList());
        }
    }

    /**
     * Activates for each player the shield indicated
     *
     * @param answer: if true the current player wants to activate shield otherwise no
     * @param shieldCoord: coordinates of the shield the current player wants to activate
     * @param batteryCoord: coordinates of the battery storage from which remove a battery
     * @return serverEvent to send to clients
     * @throws InvalidCoordinates: coordinates don't match the type of component in the shield
     * @throws NotEnoughBatteries: not enough batteries to remove were indicated by the plauer
     */
    @Override
    public ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, InvalidCoordinates, NotEnoughBatteries {
        if (state != CardState.ACTIVATE_SHIELDS) {
            throw new GeneralError("Non si può settare le decisioni nello stato" + this.state);
        }
        if(answer) {
            if (!weakestTrucker.getSpaceShip().checkShield(shieldCoord)) {
                throw new InvalidCoordinates("Not a shield");
            }
            ArrayList<int[]> l = new ArrayList<>();
            l.add(batteryCoord);
            if (!weakestTrucker.getSpaceShip().checkBatteryStorage(l)) {
                throw new InvalidCoordinates("Not a battery");
            }
            try {
                weakestTrucker.getSpaceShip().activateShield(shieldCoord, batteryCoord);
            } catch (NotEnoughBatteries e) {
                throw new NotEnoughBatteries(e.getMessage());
            }
        }
        state = CardState.ESECUZIONE;
        return cardEffect();
    }

    /**
     * finds the weakest trucker based on the current condition and handles the penalty
     * @return ServerEvent to send to clients
     */
    private ServerEvent findWeakestTrucker()throws GeneralError {
        if(state != CardState.FIND_WEAKEST){
            throw new GeneralError("FindWeakestTrucker not callable in the state " + state);
        }
        this.weakestTrucker = getWeakestTrucker(currCondition);
        if(weakestTrucker == null){
            throw new GeneralError("FindWeakestTrucker error cause weakest trucker is null");
        }
        return handlePenaltyEffect(penalties.get(currCondition).getType());
    }

    /**
     * calculcates the weakest trucker based on the condition
     * @param currCondition current condition
     * @return weakest trucker
     */
    private Trucker getWeakestTrucker(Condition currCondition) {
        Trucker currWeakestTrucker = null;
        float minValueCurrCondition = Integer.MAX_VALUE;
        for (Trucker trucker : Game.getIstance().getTruckerList()) {
            float currValue = switch (currCondition) {
                case HUMANS -> trucker.getSpaceShip().getNumCrew();
                case CANNONS -> trucker.getSpaceShip().getTotalCannonPower();
                case ENGINES -> trucker.getSpaceShip().getTotalEnginePower();
            };

            if (currValue < minValueCurrCondition) {
                minValueCurrCondition = currValue;
                currWeakestTrucker = trucker;
            }
        }
        return currWeakestTrucker;
    }

    /**
     * applies the penalty to the weakest trucker
     * @param effectType type of penalty
     * @return serverEvent to send to clients
     */
    private ServerEvent handlePenaltyEffect(TypeOfEffect effectType) throws GeneralError {
        switch (effectType) {
            case LOSE_DAYS:
                state = CardState.ESECUZIONE;
                return cardEffect();
            case GET_SHOT:
                state = CardState.ROLL_DICE;
                return new BooleanDecision(true, ParserState.ROLL_DICE, weakestTrucker.getNickname(), Game.getIstance().getTruckerList());
            case LOSE_GOODS:
                state = CardState.REMOVE_GOODS;
                return removeGoods();
            case LOSE_HUMANS:
                state = CardState.HUMANSTOREMOVE;
                numOfHumanToRemove = penalties.get(currCondition).getValue();

                if(weakestTrucker.getSpaceShip().getNumCrew() < numOfHumanToRemove){
                    weakestTrucker.getSpaceShip().removeAllHumans();
                    state = CardState.ESECUZIONE;
                    return cardEffect();
                }
                return new HumanCoordinates(true, weakestTrucker.getNickname(),weakestTrucker.getSpaceShip(),Game.getIstance().getTruckerList());
            default:
                throw new GeneralError("Unknown TypeOfEffect");
        }
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
     * applies the card effect based on the penalty and if all conditions have been verified ends the card
     * @return serverEvent to send to clients
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if(state != CardState.ESECUZIONE){
            throw new GeneralError("Card effect not callable in the state " + state);
        }
        if(weakestTrucker == null){
            throw new GeneralError("WeakestTrucker is null");
        }

        else {

            Effect effect = penalties.get(currCondition);
            switch(effect.getType()){
                case LOSE_DAYS ->{
                    Game.getIstance().moveBackward(weakestTrucker, effect.getValue());
                    deactivateTruckersSpaceShip();

                    currConditionIndex++;
                    if(currConditionIndex == conditionsOrder.size()){
                        return handleEndTurn();
                    }else{
                        currCondition = conditionsOrder.get(currConditionIndex);
                        state = CardState.WARZONE;
                        return new CurrentCondition(true, ParserState.WARZONE, Game.getIstance().getTruckerList().getFirst().getNickname(), currCondition,Game.getIstance().getTrack(), Game.getIstance().endTurn(), Game.getIstance().getTruckerList());
                    }
                }

                case LOSE_HUMANS -> {

                    deactivateTruckersSpaceShip();

                    currConditionIndex++;
                    if(currConditionIndex == conditionsOrder.size()){
                        return handleEndTurn();
                    }else{
                        currCondition = conditionsOrder.get(currConditionIndex);
                        state = CardState.WARZONE;
                        return new CurrentCondition(true, ParserState.WARZONE, Game.getIstance().getTruckerList().getFirst().getNickname(), currCondition,null, Game.getIstance().endTurn(), Game.getIstance().getTruckerList());
                    }
                }

                case LOSE_GOODS -> {
                    deactivateTruckersSpaceShip();

                    currConditionIndex++;
                    if(currConditionIndex == conditionsOrder.size()){
                        return handleEndTurn();
                    }else{
                        currCondition = conditionsOrder.get(currConditionIndex);
                        state = CardState.WARZONE;
                        return new CurrentCondition(true, ParserState.WARZONE, Game.getIstance().getTruckerList().getFirst().getNickname(), currCondition,null, Game.getIstance().endTurn(), Game.getIstance().getTruckerList());
                    }
                }

                case GET_SHOT -> {
                    SpaceShip ship = weakestTrucker.getSpaceShip();
                    boolean isHeavy = (this.shoot[0] == 1);
                    int direction = this.shoot[1];
                    if (ship.isHit(direction, this.roll)) {
                        if (isHeavy) {
                            ship.destroyComponent(this.roll, direction, Game.getIstance().getBank());
                        } else {
                            if (!ship.blockLightCannonShot(direction)) {
                                ship.destroyComponent(this.roll, direction, Game.getIstance().getBank());
                            }
                        }
                    }
                    deactivateTruckersSpaceShip();

                    currShootIndex++;

                    if(currShootIndex == shoots.size()){
                        currConditionIndex++;
                        if(currConditionIndex == conditionsOrder.size()){
                            return handleEndTurn();
                        }else{
                            currCondition = conditionsOrder.get(currConditionIndex);
                            state = CardState.WARZONE;
                            return new CurrentCondition(true, ParserState.WARZONE, Game.getIstance().getTruckerList().getFirst().getNickname(), currCondition, null, Game.getIstance().endTurn(), Game.getIstance().getTruckerList());
                        }
                    }else{
                        state = CardState.ROLL_DICE;
                        return new BooleanDecision(true, ParserState.ROLL_DICE, this.weakestTrucker.getNickname(), Game.getIstance().getTruckerList());
                    }
                }
                default -> throw new GeneralError("Unknown TypeOfEffect");
            }
        }
    }

    /**
     * handles the end of the card
     * @return serverEvent to send to clients
     */
    private ServerEvent handleEndTurn() {
        ArrayList<Trucker> truckerRemoved = Game.getIstance().endTurn();

        GamePersistence.saveGame(GameController.getIstance());
        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, Game.getIstance().getTrack());
    }

    public void setState(CardState state) {
        this.state = state;
    }

    public Map<Condition, Effect> getPenalties() {
        return penalties;
    }

    public void setCurrConditionIndex(int currConditionIndex) {
        this.currConditionIndex = currConditionIndex;
    }

    public void setIndexOfCurrTrucker(int indexOfCurrTrucker) {
        this.indexOfCurrTrucker = indexOfCurrTrucker;
    }

    public void setCurrCondition() {
        this.currCondition = conditionsOrder.get(currConditionIndex);
    }

    public Trucker getWeakestTrucker() {
        return weakestTrucker;
    }

    public void setWeakestTrucker(Trucker weakestTrucker) {
        this.weakestTrucker = weakestTrucker;
    }

    public void setColorOfCurrGoodToRemove(int colorOfCurrGoodToRemove) {
        this.colorOfCurrGoodToRemove = colorOfCurrGoodToRemove;
    }

    public void setGoodsToLose(int goodsToLose) {
        this.goodsToLose = goodsToLose;
    }

    public void setInitializeRemoveGoods(boolean initializeRemoveGoods) {
        this.initializeRemoveGoods = initializeRemoveGoods;
    }
}