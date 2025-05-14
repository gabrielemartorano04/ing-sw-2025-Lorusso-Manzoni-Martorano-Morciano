package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.callers.WarZoneCaller;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
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
    private Condition currCondition;
    private Trucker weakestTrucker;
    private Map<Condition, Effect> penalties;
    private int[] shoot;
    private int numOfGoodsToRemove;
    private int i;
    private boolean useShield;
    private int roll;

    // variabili per la remove Goods
    private int colorOfCurrGoodToRemove; // vale 0 se è rosso, 1 se è giallo, 2 se è verde e 3 se è blu
    private int numOfCurrTypeOfGood;
    private ArrayList<Goods> goodsToRemove; // verrà creata al più una volta per istanza di questa classe
    // fine variabili per la remove

    // variabile per remove Human
    private int numOfHumanToRemove;
    //fine variabile per remove Human

    private ArrayList<ArrayList<int[]>> truckersCannonsCoordinates;
    private ArrayList<ArrayList<int[]>> truckersEnginesCoordinates;


    private ArrayList<int[]> shoots;
    // shoots è una lista dove shoots.get(i) rappresenta l'i-esimo colpo in questa specifica carta Warzone
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da dx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    // memorizza le coordinate da cui rimuovere l'equipaggio in caso la penitenza sia togli num di equipaggio
    private ArrayList<int[]> coordinatesToRemoveCrew;

    // memorizza le coordinate da cui rimuovere le merci in caso la penitenza sia togli num di merci
    private ArrayList<int[]> coordinatesToRemoveGoods;


    public WarZone(boolean trialFlight, int level, ArrayList<Condition> conditionsOrder, Map<Condition, Effect> penalties, ArrayList<int[]> shoots ,String frontImage, String backImage) {
        super("War Zone", "A dangerous conflict zone", trialFlight, level, frontImage, backImage, new WarZoneCaller());
        this.conditionsOrder = conditionsOrder;
        this.penalties = penalties;
        this.shoots = shoots;
        this.state = CardState.WARZONE;
        this.currConditionIndex = 0;
        this.weakestTrucker = null;
        this.i = 0;
        this.truckersCannonsCoordinates = new ArrayList<>();
        this.truckersEnginesCoordinates = new ArrayList<>();
        this.colorOfCurrGoodToRemove = 0;
        this.numOfCurrTypeOfGood = 0;
        this.goodsToRemove = new ArrayList<>();
        this.numOfHumanToRemove = 0;
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
        if(currConditionIndex == conditionsOrder.size()){
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

    public void setCannonCoordinates(Game game, Trucker trucker, ArrayList<int[]> truckerCannonsCoordinates) throws GeneralError, InvalidComponentsException{
        if(state != CardState.WAIT_CANNONS){
            throw new GeneralError("setCannonCoordinates not callable in the state " + state);
        }
        if(truckerCannonsCoordinates == null){
            throw new GeneralError("TruckerCannonCoordinates is null");
        }
        if(!trucker.getSpaceShip().checkDoubleCannon(truckerCannonsCoordinates)){
            throw new InvalidComponentsException("Not all components are double Cannons");
        }

        truckersCannonsCoordinates.addLast(truckerCannonsCoordinates);
        if(truckersCannonsCoordinates.size() == game.getTruckerList().size()){
            state = CardState.FIND_WEAKEST;
            findWeakestTrucker(game, truckersCannonsCoordinates);
        }
    }

    public void setEnginesCoordinates(Game game, Trucker trucker, ArrayList<int[]> truckerEnginesCoordinates) throws GeneralError, InvalidComponentsException{
        if(state != CardState.WAIT_ENGINES){
            throw new GeneralError("setCannonCoordinates not callable in the state " + state);
        }
        if(truckerEnginesCoordinates == null){
            throw new GeneralError("TruckerEnginesCoordinates is null");
        }
        if(!trucker.getSpaceShip().checkDoubleCannon(truckerEnginesCoordinates)){
            throw new InvalidComponentsException("Not all components are double Cannons");
        }
        truckersEnginesCoordinates.addLast(truckerEnginesCoordinates);
        if(truckersEnginesCoordinates.size() == game.getTruckerList().size()){
            state = CardState.FIND_WEAKEST;
            findWeakestTrucker(game, truckersEnginesCoordinates);
        }
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

    public CardState manageRemoveGoods(Trucker trucker, Bank bank) throws GeneralError{
        if(state != CardState.REMOVE_GOODS){
            throw new GeneralError("plotGoodsToRemove not callable in the state " + state);
        }
        if(trucker == null){
            throw new GeneralError("trucker is null");
        }
        ArrayList<Goods> goodsList = trucker.getSpaceShip().getGoodsList();

        // se il colore corrente è minore uguale a 3 allora devo togliere le merci
        while(colorOfCurrGoodToRemove<=3 && numOfGoodsToRemove>0) {
            switch (colorOfCurrGoodToRemove) {
                case 0 -> {
                    numOfCurrTypeOfGood = trucker.getSpaceShip().getRedGoods();
                    colorOfCurrGoodToRemove++;
                }
                case 1 -> {
                    numOfCurrTypeOfGood = trucker.getSpaceShip().getYellowGoods();
                    colorOfCurrGoodToRemove++;
                }
                case 2 -> {
                    numOfCurrTypeOfGood = trucker.getSpaceShip().getGreenGoods();
                    colorOfCurrGoodToRemove++;
                }
                case 3 -> {
                    numOfCurrTypeOfGood = trucker.getSpaceShip().getBlueGoods();
                    colorOfCurrGoodToRemove++;
                }
            }
            if(numOfCurrTypeOfGood==0){
                break;
            }
            if (numOfCurrTypeOfGood <= numOfGoodsToRemove) {
                removeWithoutAsking(trucker,goodsList,bank);
            } else {
                state = CardState.PLOT_GOODSLIST;
                break;
            }
        }
        if (numOfGoodsToRemove > 0 && (trucker.getSpaceShip().getNumBatteries() > 0)) {
            if(!trucker.getSpaceShip().getGoodsList().isEmpty()){
                throw new GeneralError("GoodsList should be empty at this point");
            }
            // se il colore corrente è maggiore di 3 allora vuol dire che devo togliere batterie se ho ancora merci da rimuovere
            state = CardState.PLOT_BATTERIES;
        }else if (numOfGoodsToRemove > 0 && trucker.getSpaceShip().getNumBatteries() == 0) {
            if(!trucker.getSpaceShip().getGoodsList().isEmpty()){
                throw new GeneralError("GoodsList should be empty at this point");
            }
            state = CardState.WARZONE;
        }
        if(numOfGoodsToRemove == 0){
            state = CardState.WARZONE;
        }
        return state;

    }

    private void removeWithoutAsking(Trucker trucker, ArrayList<Goods> goodsList, Bank bank)throws GeneralError {
        if(this.numOfCurrTypeOfGood == 0){
            throw new GeneralError("NumOfGoods is 0 but you called removeWithoutAsking");
        }
        if(goodsList == null){
            throw new GeneralError("goodsList is null");
        }
        ArrayList<Goods> list = new ArrayList<>();
        for (int i = 0; i < numOfCurrTypeOfGood; i++) {
            Goods good = goodsList.getFirst();
            list.add(good);
            bank.addGoodsInBank(good.getValue());
            goodsList.removeFirst();
            numOfGoodsToRemove--;
        }
        trucker.getSpaceShip().removeGoods(list);

    }

    public ArrayList<Goods> plotGoodsList(Trucker trucker)throws GeneralError {
        if (state != CardState.PLOT_GOODSLIST){
            throw new GeneralError("plotGoodsList not callable in the state " + state);
        }
        if(trucker == null){
            throw new GeneralError("trucker is null");
        }
        if(!goodsToRemove.isEmpty()){
            throw new GeneralError("Tried to set a new goodsToRemove list but it isn't empty");
        }
        ArrayList<Goods> goodsList = trucker.getSpaceShip().getGoodsList();
        for (int i = 0; i < numOfCurrTypeOfGood; i++) {
            goodsToRemove.add(goodsList.getFirst());
        }
        state = CardState.REMOVE_ASKING;
        return goodsToRemove;
    }

    /**
     *
     * @param indexes: contains the indexes of the Goods within the list displayed on the screen that the player wants to remove from his ship
     * @param trucker
     */

    public void removeAsking(ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws GeneralError, InvalidGoodChosen, NotEnoughGoodsChosen {
        if (state != CardState.REMOVE_ASKING){
            throw new GeneralError("Can't call removeAsking in the state " + state);
        }
        if(indexes.size() == numOfGoodsToRemove){
            throw new NotEnoughGoodsChosen("The number of goods selected is not " + numOfGoodsToRemove);
        }
        for(Integer index : indexes){
            if(index < 0 || index >= goodsToRemove.size()){
                throw new InvalidGoodChosen("A good with an index out of range has been chosen");
            }
        }
        if(trucker == null){
            throw new GeneralError("trucker is null");
        }
        numOfGoodsToRemove = 0;
        ArrayList<Goods> list = new ArrayList<>();
        for(Integer index : indexes){
            list.add(goodsToRemove.get(index));
        }
        removeWithoutAsking(trucker,list, bank);
        if(currConditionIndex == conditionsOrder.size()){
            state = CardState.EXIT;
        }else {
            state = CardState.WARZONE;
        }
    }

    public int getNumOfBatteriesToRemove() throws GeneralError{
        if(state != CardState.PLOT_BATTERIES){
            throw new GeneralError("Can't call getNumOfBatteriesToRemove in the state " + state);
        }
        return numOfGoodsToRemove;
    }

    public void removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws GeneralError, InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries {
        if(state!= CardState.REMOVE_BATTERIES){
            throw new GeneralError("Can't call removeBatteries in the state " + state);
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
        if(counterOfBatteriesSelectedByPlayer != numOfGoodsToRemove){
            throw new WrongNumberOfBatteriesSelected("The total number of batteries you selected to remove is not " + numOfGoodsToRemove);
        }
        try{
            trucker.getSpaceShip().removeBatteries(coordinatesToRemoveBatteries);
        }catch(NotEnoughBatteries e){
            throw new NotEnoughBatteries("Not enough batteries");
        }
        if(currConditionIndex == conditionsOrder.size()){
            state = CardState.EXIT;
        }else {
            state = CardState.WARZONE;
        }
    }

    public int[] getCurrentMeteor() throws GeneralError{
        if(state != CardState.PLOT_SHOOT) {
            throw new GeneralError("Non si può settare le decisioni nello stato" + this.state);
        }
        if(i<0 || i >= shoots.size()){
            throw new GeneralError("Stai cercando di accedere a un colpo che non esiste");
        }
        int[] shoot = shoots.get(i);
        this.shoot = shoot;
        i++;
        state = CardState.ROLL_DICE;
        return shoot;
    }

    public void rollDice(Game game) throws GeneralError {
        if(state != CardState.ROLL_DICE){
            throw new GeneralError("Non si può girare i dadi nello stato " + this.state);
        }
        this.roll = game.rollDice();
        state = CardState.CHIEDI_SCUDI;
    }

    public void setAnswers(boolean answers) throws GeneralError {
        if(state != CardState.CHIEDI_SCUDI){
            throw new GeneralError("Non si può settare le decisioni nello stato" + this.state);
        }
        this.useShield = answers;
        state = CardState.ESECUZIONE;
    }

    public synchronized CardState removeCrew(ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, GeneralError {
        if(state != CardState.HUMANSTOREMOVE) {
            throw new GeneralError("Can't call setCoordinates in the state: " + state);
        }
        if(weakestTrucker == null){
            throw new GeneralError("trucker is null");
        }
        if(coordinates==null|| coordinates.isEmpty()){
            throw new InvalidCoordinates("Coordinates is null or empty");
        }
        try{
            weakestTrucker.getSpaceShip().checkCabin(coordinates, this.numOfHumanToRemove);
        }catch(InvalidCoordinates e){
            throw new InvalidCoordinates(e.getMessage());
        }
        this.coordinatesToRemoveCrew = coordinates;
        weakestTrucker.getSpaceShip().removeNumHuman(coordinatesToRemoveCrew);
        state = CardState.ESECUZIONE;
        return state;
    }

    @Override
    public CardState cardEffect(Game game) throws GeneralError {
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
                    game.moveBackward(weakestTrucker, effect.getValue());
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
                            ship.destroyComponent(this.roll, direction, game.getBank());
                        } else {
                            if (!ship.blockLightCannonShot(direction, this.roll, this.useShield)) {
                                ship.destroyComponent(this.roll, direction, game.getBank());
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