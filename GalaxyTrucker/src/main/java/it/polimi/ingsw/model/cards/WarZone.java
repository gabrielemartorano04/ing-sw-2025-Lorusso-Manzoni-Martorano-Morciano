package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.utilities.exceptions.*;

import java.io.Serializable;
import java.util.Map;
import java.util.ArrayList;

/**
 *
 */
public class WarZone extends Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private CardState state;
    private int currConditionIndex;
    private ArrayList<Condition> conditionsOrder;
    private Map<Condition, Effect> penalties;
    private ArrayList<int[]> shoots;
    // shoots è una lista dove shoots.get(i) rappresenta l'i-esimo colpo in questa specifica carta Warzone
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da dx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    private Condition currCondition;
    private Trucker weakestTrucker;
    private int roll;

    private int indexOfCurrTrucker;

    private int[] shoot;
    private int numOfGoodsToRemove;
    private int i;
    private boolean useShield;

    // variabili per la remove Goods
    private int colorOfCurrGoodToRemove;
    private int numOfCurrTypeOfGood;
    private ArrayList<Goods> goodsToRemove; // verrà creata al più una volta per istanza di questa classe
    // fine variabili per la remove

    // variabile per remove Human
    private int numOfHumanToRemove;
    //fine variabile per remove Human


    // memorizza le coordinate da cui rimuovere l'equipaggio in caso la penitenza sia togli num di equipaggio
    private ArrayList<int[]> coordinatesToRemoveCrew;

    // memorizza le coordinate da cui rimuovere le merci in caso la penitenza sia togli num di merci
    private ArrayList<int[]> coordinatesToRemoveGoods;


    public WarZone(boolean trialFlight, int level, ArrayList<Condition> conditionsOrder, Map<Condition, Effect> penalties, ArrayList<int[]> shoots ,String frontImage, String backImage) {
        super("War Zone", "A dangerous conflict zone", trialFlight, level, frontImage, backImage);
        this.conditionsOrder = conditionsOrder;
        this.penalties = penalties;
        this.shoots = shoots;
        this.state = CardState.WARZONE;
        this.currConditionIndex = 0;
        this.weakestTrucker = null;
        this.i = 0;
        this.colorOfCurrGoodToRemove = 0;
        this.numOfCurrTypeOfGood = 0;
        this.goodsToRemove = new ArrayList<>();
        this.numOfHumanToRemove = 0;
        this.indexOfCurrTrucker = 0;
    }

    public ArrayList<Condition> getConditionsOrder() {
        return conditionsOrder;
    }

    public Map<Condition, Effect> getPenalties() {
        return penalties;
    }

    public ArrayList<int[]> getShoots(){
        return shoots;
    }

    @Override
    public ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Do you want to activate your doubleCannons?",
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
                this.getCardState()
        );
    }


    public CardState combatZone(Game game) throws GeneralError{
        if(state != CardState.WARZONE){
            throw new GeneralError("CombatZone not callable in the state " + state);
        }
        if(game.getTruckerList().size() == 1){
            state = CardState.EXIT;
            return this.state;
        }
        this.currCondition = conditionsOrder.get(currConditionIndex);
        currConditionIndex++;
        if(currConditionIndex-1 == conditionsOrder.size()){
            throw new GeneralError("All conditions have been verified");
        }
        switch(currCondition){
            case HUMANS -> {
                state = CardState.FIND_WEAKEST;
                findWeakestTrucker(game, new ArrayList<>());
            }
            case CANNONS -> {
                state = CardState.WAIT_CANNONS;
            }
            case ENGINES -> {
                state = CardState.WAIT_ENGINES;
            }
        }
        return this.state;
    }


    @Override
    public ServerEvent activateCannons(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws GeneralError, InvalidComponentsException, WrongCallerException, InvalidCoordinates, NotEnoughBatteries {
        if (this.state != CardState.ACTIVATE_ENGINES) {
            throw new GeneralError("Non è possibile inserire le informazioni nello stato " + this.state);
        }
        if(activate){
            if(!Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().checkBatteryStorage(batteryCoord)){
                throw new InvalidCoordinates("Not all coordinates are battery storage");
            }
            if(!Game.getIstance().getTruckerList().get(indexOfCurrTrucker).getSpaceShip().checkDoubleCannon(coord)){
                if(Game.getIstance().getTruckerList().get(indexOfCurrTrucker).getSpaceShip().getNumDoubleCannon() == 0 ){
                    indexOfCurrTrucker++;
                    throw new InvalidCoordinates("You don't have any double engines");

                }
                throw new InvalidCoordinates("Not all components are DoubleEngine");
            }

            Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().activateDoubleCannons(coord, batteryCoord);

        }

        if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()) {
            this.state = CardState.ESECUZIONE;
            indexOfCurrTrucker=0;
            return cardEffect();
        }
        indexOfCurrTrucker++;
        return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, this.getCardState(), "vuoi attivare un Cannone Doppio?", Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
    }

    /**
     * Sets for each trucker the coordinates of the double engines that he/she wants to activate
     * @param coord list of arrays of coordinates
     */
    @Override
    public ServerEvent activateEngines(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws InvalidCoordinates, GeneralError, NotEnoughBatteries {
        if (this.state != CardState.ACTIVATE_ENGINES) {
            throw new GeneralError("Non è possibile inserire le informazioni nello stato " + this.state);
        }
        if(activate){
            if(!Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().checkBatteryStorage(batteryCoord)){
                throw new InvalidCoordinates("Not all coordinates are battery storage");
            }
            if(!Game.getIstance().getTruckerList().get(indexOfCurrentTrucker).getSpaceShip().checkDoubleEngine(coord)){
                if(Game.getIstance().getTruckerList().get(indexOfCurrentTrucker).getSpaceShip().getNumDoubleEngine() == 0 ){
                    indexOfCurrentTrucker++;
                    throw new InvalidCoordinates("You don't have any double engines");

                }
                throw new InvalidCoordinates("Not all components are DoubleEngine");
            }

            Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().activateDoubleEngines(coord, batteryCoord);

        }

        if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()) {
            this.state = CardState.ESECUZIONE;
            return cardEffect();
        }
        indexOfCurrentTrucker++;
        return new BooleanDecision(ServerEventType.ACTIVATE_ENGINES, true, this.getCardState(), "Choose the position of the double engines to activate", Game.getIstance().getTruckerList().get(indexOfCurrentTrucker).getNickname());
    }

    // questo metodo dovrà essere chiamato dal Controller per capire a ogni condizione il weakestTrucker
    // truckersCoordinates serve perché quando devo calcolare il weakest in base a potenza di fuoco o motrice
    // devo dare in ingresso a findWeakestTrucker la lista delle coordinate delle componenti che ogni giocatore
    // ha deciso di attivare
    public void findWeakestTrucker(Game game, ArrayList<ArrayList<int[]>> truckersCoordinates)throws GeneralError {
        ArrayList<Trucker> truckers = game.getTruckerList();
        if(state != CardState.FIND_WEAKEST){
            throw new GeneralError("FindWeakestTrucker not callable in the state " + state);
        }

        if(currCondition != Condition.HUMANS && truckersCoordinates.isEmpty()){
            throw new GeneralError("Not expecting an empty list of Coordinates with the condition " + currCondition);
        }

        Trucker weakestTrucker = null;
        int i = 0;
        int minValueCurrCondition = Integer.MAX_VALUE;
        for (Trucker trucker : truckers) {
            int currValue = switch (currCondition) {
                case HUMANS -> trucker.getSpaceShip().getNumHuman();
                case CANNONS -> trucker.getSpaceShip().getTotalCannonPower(truckersCoordinates.get(i));
                case ENGINES -> trucker.getSpaceShip().getTotalEnginePower(truckersCoordinates.get(i));
            };

            // in caso di parità di criteri subisce la penitenza quello più avanti nel percorso quindi quando cerco
            // il weakestTrucker nei confronti uso il minore stretto in modo che a parità di potenza di fuoco/motrice o
            // numero di umani scelgo sempre il trucker più a sinistra nella lista cioè quello più avanti sulla plancia
            if (currValue < minValueCurrCondition) {
                minValueCurrCondition = currValue;
                weakestTrucker = trucker;
            }
            i++;
        }
        this.weakestTrucker = weakestTrucker;
        if(penalties.get(currCondition).getType().equals(TypeOfEffect.LOSE_DAYS)){
            state = CardState.ESECUZIONE;
            cardEffect(game);
        }else if(penalties.get(currCondition).getType().equals(TypeOfEffect.GET_SHOT)){
            state = CardState.PLOT_SHOOT;

        } else{
            state = CardState.ATTESA_RISPOSTE;
        }
    }

    @Override
    public ServerEvent removeGoods() throws GeneralError{
        if(cardState != CardState.REMOVE_GOODS){
            throw new GeneralError("plotGoodsToRemove not callable in the state " + cardState);
        }

        if(indexOfCurrentTrucker == defeatedTruckers.size()){
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }

        ArrayList<Goods> goodsList;
        goodsList = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getGoodsList();

        while (colorOfCurrGoodToRemove > 0 && goodsLostCurrTrucker > 0) {
            switch (colorOfCurrGoodToRemove) {
                case 4 -> {
                    numGoodCurrType = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getRedGoods();
                    colorOfCurrGoodToRemove--;
                }
                case 3 -> {
                    numGoodCurrType = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getYellowGoods();
                    colorOfCurrGoodToRemove--;
                }
                case 2 -> {
                    numGoodCurrType = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getGreenGoods();
                    colorOfCurrGoodToRemove--;
                }
                case 1 -> {
                    numGoodCurrType = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getBlueGoods();
                    colorOfCurrGoodToRemove--;
                }
            }

            if (numGoodCurrType > 0 && numGoodCurrType <= goodsLostCurrTrucker) {
                return removeWithoutAsking(goodsList);
            }else if (numGoodCurrType > 0){
                cardState = CardState.REMOVE_ASKING;
                ArrayList<Goods> listOfGoods = defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getGoodsList();
                for (int i = 0; i < numGoodCurrType; i++) {
                    goodsToRemove.add(listOfGoods.getFirst());
                }
                return new RemoveGoods(true, this.cardState, defeatedTruckers.get(indexOfCurrentTrucker).getNickname(), goodsToRemove);
            }
        }

        // se sei uscito perche non avevi merci devo controllare se puoi perdere batterie
        if(colorOfCurrGoodToRemove == 0 && goodsLostCurrTrucker > 0){
            if(defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getNumBatteries() == 0){
                indexOfCurrentTrucker++;
                if(indexOfCurrentTrucker == defeatedTruckers.size()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    return new RemotionConfirmed(this.cardState, defeatedTruckers.get(indexOfCurrentTrucker-1).getNickname(), "You didn't have batteries or goods to be removed. Press continue!");
                }
            }else{
                return new RemoveBatteries(true, this.cardState, defeatedTruckers.get(indexOfCurrentTrucker).getNickname(), goodsLostCurrTrucker);
            }
        }else{
            throw new GeneralError("RemoveGoods not working correctly");
        }
    }

    private ServerEvent removeWithoutAsking(ArrayList<Goods> goodsList) throws GeneralError {
        if (goodsList == null) {
            throw new GeneralError("Remove without asking: goodsList is null");
        }
        ArrayList<Goods> list = new ArrayList<>();
        for (int i = 0; i < numGoodCurrType; i++) {
            Goods good = goodsList.getFirst();
            list.add(good);
            Game.getIstance().getBank().addGoodsInBank(good.getValue());
            //TODO: Check che sto rimuovendo effettivamente dalla lista della nave
            goodsList.removeFirst();
            goodsLostCurrTrucker--;
        }
        defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().removeGoods(list);

        if (goodsLostCurrTrucker == 0 || (goodsLostCurrTrucker > 0 && defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().getNumBatteries() == 0)) {
            // se questo trucker ha perso tutte le merci notifico che la rimozione è stata fatta e passo al prossimo
            // per passare al prossimo obbligo il corrente a mandarmi un ok/continue
            indexOfCurrentTrucker++;
            cardState = CardState.REMOVE_GOODS;
            return new RemotionConfirmed(this.cardState, defeatedTruckers.get(indexOfCurrentTrucker - 1).getNickname(), "Goods removed, press continue!");
        } else { // il trucker corrente deve perdere batterie
            cardState = CardState.REMOVE_BATTERIES;
            return new RemoveBatteries(true, this.cardState, defeatedTruckers.get(indexOfCurrentTrucker).getNickname(), goodsLostCurrTrucker);
        }
    }

    /**
     *
     * @param indexesToRemove: contains the indexes of the Goods within the list displayed on the screen that the player wants to remove from his ship
     */

    @Override
    public ServerEvent removeAsking(ArrayList<Integer> indexesToRemove) throws InvalidGoodChosen, NotEnoughGoodsChosen, GeneralError {
        if (cardState != CardState.REMOVE_ASKING){
            throw new GeneralError("Can't call removeAsking in the state " + cardState);
        }
        if(indexesToRemove.size() != goodsLostCurrTrucker){
            throw new NotEnoughGoodsChosen("The number of goods selected is not " + goodsLostCurrTrucker);
        }
        for(Integer index : indexesToRemove){
            if(index < 0 || index >= goodsToRemove.size()){
                throw new InvalidGoodChosen("A good with an index out of range has been chosen");
            }
        }
        goodsLostCurrTrucker = 0;
        ArrayList<Goods> list = new ArrayList<>();
        for(Integer index : indexesToRemove){
            list.add(goodsToRemove.get(index));
        }
        goodsToRemove.clear();
        cardState = CardState.REMOVE_GOODS;
        return removeWithoutAsking(list);
    }

    /**
     *
     * @param coordinatesToRemoveBatteries: mi aspetto che ogni elemento dell'arraylist sia [x,y,z] dove x,y sono le coordinate del batteryStorage e z è il numero di batterie da rimuovere da quel battery storage
     * @return
     * @throws GeneralError
     * @throws InvalidCoordinates
     * @throws WrongNumberOfBatteriesSelected
     * @throws NotEnoughBatteries
     */


    @Override
    public ServerEvent removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries) throws GeneralError,InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries {
        if(cardState!= CardState.REMOVE_BATTERIES){
            throw new GeneralError("Can't call removeBatteries in the state " + cardState);
        }
        if(!defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().checkBatteryStorage(coordinatesToRemoveBatteries)){
            throw new InvalidCoordinates("Not all components selected are Battery Storages");
        }
        int counterOfBatteriesSelectedByPlayer = 0;
        for(int i = 0; i < coordinatesToRemoveBatteries.size(); i++){
            counterOfBatteriesSelectedByPlayer += coordinatesToRemoveBatteries.get(i)[2];
        }
        if(counterOfBatteriesSelectedByPlayer != goodsLostCurrTrucker){
            throw new WrongNumberOfBatteriesSelected("The total number of batteries you selected to remove is not " + goodsLostCurrTrucker);
        }
        try{
            defeatedTruckers.get(indexOfCurrentTrucker).getSpaceShip().removeBatteries(coordinatesToRemoveBatteries);
        }catch(NotEnoughBatteries e){
            throw new NotEnoughBatteries("There is a battery storage that has less batteries then the number of batteries you wanted to remove in that one");
        }

        indexOfCurrentTrucker++;
        cardState = CardState.REMOVE_GOODS;
        return new RemotionConfirmed(this.cardState, defeatedTruckers.get(indexOfCurrentTrucker - 1).getNickname(), "Batteries removed, press continue!");
    }

    @Override
    public ServerEvent rollDice() throws GeneralError {
        if(state != CardState.ROLL_DICE) {
            throw new GeneralError("Non si può girare i dadi nello stato " + this.state);
        }
        if(currMeteorIndex <0 || currMeteorIndex >= meteors.size()){
            throw new GeneralError("Stai cercando di accedere a un meteorite che non esiste, la carta dovrebbe essere finita");
        }
        meteor = meteors.get(currMeteorIndex);
        currMeteorIndex++;
        this.roll = Game.getIstance().rollDice();
        state = CardState.ATTESA_RISPOSTE;
        if (meteor[0]==1) {
            state=CardState.ACTIVATE_CANNONS;
            return new RollDiceResult("Meteorite grande, attivare Cannoni doppi?", this.getCardState(), currMeteorIndex, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true);
        }else{
            state=CardState.ACTIVATE_SHIELDS;
            return new RollDiceResult("Meteorite piccolo, attivare Scudo?", this.getCardState(), currMeteorIndex, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true);
        }
    }

    /**
     * Sets the coordinates of the cabins from which he wants to remove the crew members required
     * @param coordinates list of arrays of coordinates of the cabins
     */
    @Override
    public ServerEvent setHumansCoordinates(ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, GeneralError {
        if(state != CardState.HUMANSTOREMOVE) {
            throw new GeneralError("Can't call setCoordinates in the state: " + state);
        }
        if(truckerThatHasDecided ==null){
            throw new GeneralError("Tried to set coordinates when truckerThatHasDecided is null");
        }
        try{
            truckerThatHasDecided.getSpaceShip().checkCabin(coordinates, numHuman);
            this.coordinates = coordinates;
            state = CardState.ESECUZIONE;
            return cardEffect();
        }catch (InvalidCoordinates e){
            throw new InvalidCoordinates(e.getMessage());
        }
    }

    @Override
    public ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, InvalidCoordinates {
        if (state != CardState.ACTIVATE_SHIELDS) {
            throw new GeneralError("Non si può settare le decisioni nello stato" + this.state);
        }
        this.answers.addLast(answer);
        if(answer) {
            if (!Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().checkShield(shieldCoord)) {
                throw new InvalidCoordinates("Not a shield");
            }
            ArrayList<int[]> l = new ArrayList<>();
            l.add(batteryCoord);
            if (!Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().checkBatteryStorage(l)) {
                throw new InvalidCoordinates("Not a battery");
            }
            Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().activateShield(shieldCoord, batteryCoord);
        }

        if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()) {
            state = CardState.ESECUZIONE;
            indexOfCurrTrucker = 0;
            return cardEffect();
        }
        indexOfCurrTrucker++;
        return new BooleanDecision(ServerEventType.ACTIVATE_SHIELD, true, this.getCardState(), "vuoi attivare uno scudo?", Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
    }

    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if(state != CardState.ESECUZIONE){
            throw new GeneralError("Card effect not callable in the state " + state);
        }
        if(weakestTrucker == null){
            throw new GeneralError("WeakestTrucker is null");
        }else {
            // devo gestire le ulteriori richieste per i casi dove vieni sparato perché devo chiedere al giocatore
            // se vuole attivare lo scudo ogni volta che il colpo è piccolo
            // e per il caso dove devi perdere equipaggio che devi scegliere da dove perdere umani o alieni
            Effect effect = penalties.get(currCondition);
            switch(effect.getType()){
                case LOSE_DAYS ->{
                    Game.getIstance().moveBackward(weakestTrucker, effect.getValue());
                    state = CardState.WARZONE;
                }

                case LOSE_HUMANS -> {
                    this.numOfHumanToRemove = penalties.get(currCondition).getValue();
                    state = CardState.HUMANSTOREMOVE;
                }

                case LOSE_GOODS -> {
                    numOfGoodsToRemove = effect.getValue();
                    this.state = CardState.REMOVE_GOODS;
                }

                case GET_SHOT -> {
                    SpaceShip ship = weakestTrucker.getSpaceShip();
                    boolean isHeavy = (this.shoot[0] == 1);
                    int direction = this.shoot[1];
                    if (ship.isHit(this.roll, direction)) {
                        if (isHeavy) {
                            ship.destroyComponent(this.roll, direction, Game.getIstance().getBank());
                        } else {
                            if (!ship.blockLightCannonShot(direction, this.roll, this.useShield)) {
                                ship.destroyComponent(this.roll, direction, Game.getIstance().getBank());
                            }
                        }
                    }
                    state = CardState.PLOT_SHOOT;
                    if(i == shoots.size()){
                        state = CardState.WARZONE;
                    }
                }
            }
            if(currConditionIndex == conditionsOrder.size()){
                state = CardState.EXIT;
            }
        }
        return state;
    }

    public CardState getCardState() {
        return state;
    }

    public void setCardState(CardState cardState) {
        this.state = cardState;
    }



}