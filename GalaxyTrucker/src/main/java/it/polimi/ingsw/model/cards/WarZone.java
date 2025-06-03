package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
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
    private CardState state;
    private int currConditionIndex;
    private final ArrayList<Condition> conditionsOrder;
    private final Map<Condition, Effect> penalties;
    private final ArrayList<int[]> shoots;
    // shoots è una lista dove shoots.get(i) rappresenta l'i-esimo colpo in questa specifica carta Warzone
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da dx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    private Condition currCondition;
    private Trucker weakestTrucker;
    private int roll;

    private int indexOfCurrTrucker;

    private int currShootIndex;
    private int[] shoot;
    private boolean useShield;

    // variabili per la remove Goods
    private int colorOfCurrGoodToRemove;
    private int numGoodCurrType;
    private final ArrayList<Goods> goodsToRemove; // verrà creata al più una volta per istanza di questa classe
    private int goodsLostCurrTrucker;

    // variabile per remove Human
    private int numOfHumanToRemove;
    private ArrayList<int[]> humanCoordinates;
    //fine variabile per remove Human



    public WarZone(boolean trialFlight, int level, ArrayList<Condition> conditionsOrder, Map<Condition, Effect> penalties, ArrayList<int[]> shoots , String frontImage, String backImage) {
        super("War Zone", "A dangerous conflict zone", trialFlight, level, frontImage, backImage);
        this.conditionsOrder = conditionsOrder;
        this.penalties = penalties;
        this.shoots = shoots;
        this.state = CardState.WARZONE;
        this.currConditionIndex = 0;
        this.weakestTrucker = null;
        this.indexOfCurrTrucker = 0;
        this.colorOfCurrGoodToRemove = 0;
        this.numGoodCurrType = 0;
        this.goodsLostCurrTrucker = 0;
        this.currShootIndex = 0;
        this.currCondition = null;
        this.numOfHumanToRemove = 0;

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
                ParserState.WARZONE
        );
    }

    @Override
    public ServerEvent handleCurrentCondition() throws GeneralError{
        if(state != CardState.WARZONE){
            throw new GeneralError("CombatZone not callable in the state " + state);
        }
        if(Game.getIstance().getTruckerList().size() == 1){
            ArrayList<String> truckerRemoved;
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
        this.currCondition = conditionsOrder.get(currConditionIndex);
        switch(currCondition){
            case HUMANS -> {
                state = CardState.FIND_WEAKEST;
                return findWeakestTrucker();
            }
            case CANNONS -> {
                state = CardState.ACTIVATE_CANNONS;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, ParserState.ACTIVATE_CANNONS, "Activate double cannons?", Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
            }
            case ENGINES -> {
                state = CardState.ACTIVATE_ENGINES;
                return new BooleanDecision(ServerEventType.ACTIVATE_ENGINES, true, ParserState.ACTIVATE_ENGINES, "Activate double engines?", Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
            }
            default ->
                throw new GeneralError("Unknown condition " + currCondition);

        }
    }


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
        return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, ParserState.ACTIVATE_CANNONS, "vuoi attivare un Cannone Doppio?", Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
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
        return new BooleanDecision(ServerEventType.ACTIVATE_ENGINES, true, ParserState.ACTIVATE_ENGINES, "Choose the position of the double engines to activate", Game.getIstance().getTruckerList().get(indexOfCurrTrucker).getNickname());
    }


    @Override
    public ServerEvent removeGoods() throws GeneralError{
        if(state != CardState.REMOVE_GOODS){
            throw new GeneralError("removeGoods not callable in the state " + state);
        }

        if(weakestTrucker == null){
            throw new GeneralError("removeGoods error - weakest trucker is null");
        }

        ArrayList<Goods> goodsList;
        goodsList = weakestTrucker.getSpaceShip().getGoodsList();
        if(penalties.get(currCondition).getType() != TypeOfEffect.LOSE_GOODS){
            throw new GeneralError("removegoods error - curr penalty is not lose goods");
        }
        goodsLostCurrTrucker = penalties.get(currCondition).getValue();
        while (colorOfCurrGoodToRemove > 0 && goodsLostCurrTrucker > 0) {
            switch (colorOfCurrGoodToRemove) {
                case 4 -> {
                    numGoodCurrType = weakestTrucker.getSpaceShip().getRedGoods();
                    colorOfCurrGoodToRemove--;
                }
                case 3 -> {
                    numGoodCurrType = weakestTrucker.getSpaceShip().getYellowGoods();
                    colorOfCurrGoodToRemove--;
                }
                case 2 -> {
                    numGoodCurrType = weakestTrucker.getSpaceShip().getGreenGoods();
                    colorOfCurrGoodToRemove--;
                }
                case 1 -> {
                    numGoodCurrType = weakestTrucker.getSpaceShip().getBlueGoods();
                    colorOfCurrGoodToRemove--;
                }
            }

            if (numGoodCurrType > 0 && numGoodCurrType <= goodsLostCurrTrucker) {
                return removeWithoutAsking(goodsList);
            }else if (numGoodCurrType > 0){
                state = CardState.REMOVE_ASKING;
                ArrayList<Goods> listOfGoods = weakestTrucker.getSpaceShip().getGoodsList();
                for (int i = 0; i < numGoodCurrType; i++) {
                    goodsToRemove.add(listOfGoods.getFirst());
                }
                return new RemoveGoods(true, weakestTrucker.getNickname(), goodsToRemove);
            }
        }

        if(colorOfCurrGoodToRemove == 0 && goodsLostCurrTrucker > 0){
            if(weakestTrucker.getSpaceShip().getNumBatteries() == 0){
                this.state = CardState.WARZONE;
                currConditionIndex++;
                return new RemotionConfirmed(ServerEventType.GOODS_REMOVED, weakestTrucker.getNickname(), "You didn't have batteries or goods to be removed. Press continue!");

            }else{
                this.state = CardState.REMOVE_BATTERIES;
                return new RemoveBatteries(true, weakestTrucker.getNickname(), goodsLostCurrTrucker);
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
        weakestTrucker.getSpaceShip().removeGoods(list);

        if (goodsLostCurrTrucker == 0 || (goodsLostCurrTrucker > 0 && weakestTrucker.getSpaceShip().getNumBatteries() == 0)) {
            // se questo trucker ha perso tutte le merci notifico che la rimozione è stata fatta e passo al prossimo
            // per passare al prossimo obbligo il corrente a mandarmi un ok/continue
            state = CardState.ESECUZIONE;
            return cardEffect();
        } else { // il trucker corrente deve perdere batterie
            state = CardState.REMOVE_BATTERIES;
            return new RemoveBatteries(true, weakestTrucker.getNickname(), goodsLostCurrTrucker);
        }
    }

    /**
     *
     * @param indexesToRemove: contains the indexes of the Goods within the list displayed on the screen that the player wants to remove from his ship
     */

    @Override
    public ServerEvent removeAsking(ArrayList<Integer> indexesToRemove) throws InvalidGoodChosen, NotEnoughGoodsChosen, GeneralError {
        if (state != CardState.REMOVE_ASKING){
            throw new GeneralError("Can't call removeAsking in the state " + state);
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
            list.add(goodsToRemove.get(index)); // TODO: CHECK se è corretto l'uso di integer qui
        }
        goodsToRemove.clear();
        state = CardState.REMOVE_GOODS;
        return removeWithoutAsking(list);
    }

    /**
     *
     * @param coordinatesToRemoveBatteries: mi aspetto che ogni elemento dell'arraylist sia [x,y,z] dove x,y sono le coordinate del batteryStorage e z è il numero di batterie da rimuovere da quel battery storage
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
        if(counterOfBatteriesSelectedByPlayer != goodsLostCurrTrucker){
            throw new WrongNumberOfBatteriesSelected("The total number of batteries you selected to remove is not " + goodsLostCurrTrucker);
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
     * Sets the coordinates of the cabins from which he wants to remove the crew members required
     * @param coordinates list of arrays of coordinates of the cabins
     */
    @Override
    public ServerEvent setHumansCoordinates(ArrayList<int[]> coordinates) throws InvalidCoordinates, GeneralError {
        if(state != CardState.HUMANSTOREMOVE) {
            throw new GeneralError("Can't call setCoordinates in the state: " + state);
        }
        if(weakestTrucker ==null){
            throw new GeneralError("Tried to set coordinates when truckerThatHasDecided is null");
        }
        try{
            weakestTrucker.getSpaceShip().checkCabin(coordinates, numOfHumanToRemove);
            this.humanCoordinates = coordinates;
            state = CardState.ESECUZIONE;
            return cardEffect();
        }catch (InvalidCoordinates e){
            throw new InvalidCoordinates(e.getMessage());
        }
    }

    @Override
    public ServerEvent rollDice() throws GeneralError {
        if(state != CardState.ROLL_DICE) {
            throw new GeneralError("Non si può girare i dadi nello stato " + this.state);
        }
        if(currShootIndex <0 || currShootIndex >= shoots.size()){
            throw new GeneralError("Stai cercando di accedere a un meteorite che non esiste, la carta dovrebbe essere finita");
        }
        shoot = shoots.get(currShootIndex);
        currShootIndex++;
        this.roll = Game.getIstance().rollDice();
        if (shoot[0]==1) {
            state = CardState.ESECUZIONE;
            return cardEffect();
        }else{
            state=CardState.ACTIVATE_SHIELDS;
            return new RollDiceResult("Meteorite piccolo, attivare Scudo?", currConditionIndex, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true, "smallmeteor");
        }
    }

    @Override
    public ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, InvalidCoordinates {
        if (state != CardState.ACTIVATE_SHIELDS) {
            throw new GeneralError("Non si può settare le decisioni nello stato" + this.state);
        }
        useShield = answer;
        if(answer) {
            if (!weakestTrucker.getSpaceShip().checkShield(shieldCoord)) {
                throw new InvalidCoordinates("Not a shield");
            }
            ArrayList<int[]> l = new ArrayList<>();
            l.add(batteryCoord);
            if (!weakestTrucker.getSpaceShip().checkBatteryStorage(l)) {
                throw new InvalidCoordinates("Not a battery");
            }
            weakestTrucker.getSpaceShip().activateShield(shieldCoord, batteryCoord);
        }
        state = CardState.ESECUZIONE;
        return cardEffect();
    }

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

    private ServerEvent handlePenaltyEffect(TypeOfEffect effectType) throws GeneralError {
        switch (effectType) {
            case LOSE_DAYS:
                state = CardState.ESECUZIONE;
                return cardEffect();
            case GET_SHOT:
                state = CardState.ROLL_DICE;
                return new BooleanDecision(ServerEventType.ROLL_DICE, true, ParserState.ROLL_DICE, "Roll the dice", weakestTrucker.getNickname());
            case LOSE_GOODS:
                state = CardState.REMOVE_GOODS;
                return removeGoods();
            case LOSE_HUMANS:
                state = CardState.HUMANSTOREMOVE;
                numOfHumanToRemove = penalties.get(currCondition).getValue();
                return new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, true, weakestTrucker.getNickname());
            default:
                throw new GeneralError("Unknown TypeOfEffect");
        }
    }


    private Trucker getWeakestTrucker(Condition currCondition) {
        Trucker currWeakestTrucker = null;
        int minValueCurrCondition = Integer.MAX_VALUE;
        for (Trucker trucker : Game.getIstance().getTruckerList()) {
            int currValue = switch (currCondition) {
                case HUMANS -> trucker.getSpaceShip().getNumCrew();
                case CANNONS -> trucker.getSpaceShip().getTotalCannonPower();
                case ENGINES -> trucker.getSpaceShip().getTotalEnginePower();
            };

            // in caso di parità di criteri subisce la penitenza quello più avanti nel percorso quindi quando cerco
            // il weakestTrucker nei confronti uso il minore stretto in modo che a parità di potenza di fuoco/motrice o
            // numero di umani scelgo sempre il trucker più a sinistra nella lista cioè quello più avanti sulla plancia
            if (currValue < minValueCurrCondition) {
                minValueCurrCondition = currValue;
                currWeakestTrucker = trucker;
            }
        }
        return currWeakestTrucker;
    }

    private void deactivateTruckersSpaceShip(){
        for(Trucker trucker: Game.getIstance().getTruckerList()){
            trucker.getSpaceShip().deactivateDoubleCannons();
            trucker.getSpaceShip().deactivateDoubleEngines();
            trucker.getSpaceShip().deActivateShields();
        }
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
                    currConditionIndex++;
                    deactivateTruckersSpaceShip();
                    if(currConditionIndex == conditionsOrder.size()){
                        ArrayList<String> truckerRemoved;
                        truckerRemoved = Game.getIstance().endTurn();
                        Map<String, Integer> track = new HashMap<>();
                        for (Trucker t : Game.getIstance().getTruckerList()) {
                            track.put(t.getNickname(), t.getPosition());
                        }
                        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track);
                    }else{
                        state = CardState.WARZONE;
                        return new CurrentCondition(true, ParserState.WARZONE, Game.getIstance().getTruckerList().getFirst().getNickname(), currCondition);
                    }
                }

                case LOSE_HUMANS -> {
                    weakestTrucker.getSpaceShip().removeNumHuman(humanCoordinates);
                    currConditionIndex++;
                    deactivateTruckersSpaceShip();
                    if(currConditionIndex == conditionsOrder.size()){
                        ArrayList<String> truckerRemoved;
                        truckerRemoved = Game.getIstance().endTurn();
                        Map<String, Integer> track = new HashMap<>();
                        for (Trucker t : Game.getIstance().getTruckerList()) {
                            track.put(t.getNickname(), t.getPosition());
                        }
                        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track);
                    }else{
                        state = CardState.WARZONE;
                        return new CurrentCondition(true, ParserState.WARZONE, Game.getIstance().getTruckerList().getFirst().getNickname(), currCondition);
                    }
                }

                case LOSE_GOODS -> {
                    // dovrebbe essere già gestito fuori
                    currConditionIndex++;
                    deactivateTruckersSpaceShip();
                    if(currConditionIndex == conditionsOrder.size()){
                        ArrayList<String> truckerRemoved;
                        truckerRemoved = Game.getIstance().endTurn();
                        Map<String, Integer> track = new HashMap<>();
                        for (Trucker t : Game.getIstance().getTruckerList()) {
                            track.put(t.getNickname(), t.getPosition());
                        }
                        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track);
                    }else{
                        state = CardState.WARZONE;
                        return new CurrentCondition(true, ParserState.WARZONE, Game.getIstance().getTruckerList().getFirst().getNickname(), currCondition);
                    }
                }

                case GET_SHOT -> {
                    SpaceShip ship = weakestTrucker.getSpaceShip();
                    boolean isHeavy = (this.shoot[0] == 1);
                    int direction = this.shoot[1];
                    if (ship.isHit(this.roll, direction)) {
                        if (isHeavy) {
                            ship.destroyComponent(this.roll, direction, Game.getIstance().getBank());
                        } else {
                            if (!ship.blockLightCannonShot(direction, this.useShield)) {
                                ship.destroyComponent(this.roll, direction, Game.getIstance().getBank());
                            }
                        }
                    }
                    currShootIndex++;
                    deactivateTruckersSpaceShip();
                    if(currShootIndex == shoots.size()){
                        currConditionIndex++;
                        if(currConditionIndex == conditionsOrder.size()){
                            ArrayList<String> truckerRemoved;
                            truckerRemoved = Game.getIstance().endTurn();
                            Map<String, Integer> track = new HashMap<>();
                            for (Trucker t : Game.getIstance().getTruckerList()) {
                                track.put(t.getNickname(), t.getPosition());
                            }
                            return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track);
                        }else{
                            state = CardState.WARZONE;
                            return new CurrentCondition(true, ParserState.WARZONE, Game.getIstance().getTruckerList().getFirst().getNickname(), currCondition);
                        }
                    }else{
                        state = CardState.ROLL_DICE;
                        return new BooleanDecision(ServerEventType.ROLL_DICE, true, ParserState.ROLL_DICE, "Roll the dice", this.weakestTrucker.getNickname());
                    }
                }
                default -> throw new GeneralError("Unknown TypeOfEffect");
            }
        }
    }



}