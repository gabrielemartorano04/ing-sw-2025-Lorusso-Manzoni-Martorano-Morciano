package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


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
    private final int smugglersPower;
    private final int goodsLost;
    private final int days;
    private final Goods[] goodsGained;

    private CardState cardState;
    private int indexOfCurrentTrucker;
    private Trucker winnerTrucker; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private final ArrayList<Trucker> defeatedTruckers;
    private boolean winDecision;

    private int goodsLostCurrTrucker;
    private int currGoodIndex;

    // variabili per la remove
    private int colorOfCurrGoodToRemove;
    private int numGoodCurrType;
    private final ArrayList<Goods> goodsToRemove;// verrà creata al più una volta per istanza di questa classe
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
        this.cardState = CardState.ACTIVATE_CANNONS;
        this.indexOfCurrentTrucker = 0;
        this.winnerTrucker =null;
        this.winDecision=false;
        this.currGoodIndex = 0;
        this.colorOfCurrGoodToRemove = 4;
        this.numGoodCurrType = 0;
        this.goodsToRemove = new ArrayList<>();
        this.defeatedTruckers = new ArrayList<>();
        this.goodsLostCurrTrucker = goodsLost;
    }

//    /**
//     * Returns the cannon power required
//     * @return cannon power required
//     */
//    public int getSmugglersPower() {
//        return smugglersPower;
//    }

//    /**
//     * Returns the number of goods to lose
//     * @return goods to lose
//     */
//    public int getGoodsLost() {
//        return goodsLost;
//    }

//    /**
//     * Returns an array of goods to gain
//     * @return array of goods to gain
//     */
//    public Goods[] getGoodsGained() {
//        return goodsGained;
//    }

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
        System.out.println(Game.getIstance().getTruckerList().getFirst().getNickname());
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
                getFrontImagePath()
        );
    }


    private ServerEvent fightIsWin() throws GeneralError {
        if (Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() > smugglersPower) {
            winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
            cardState = CardState.GOODSTOADD;
            System.out.println("11");
            return new PlotGood(true, winnerTrucker.getNickname(),winnerTrucker.getSpaceShip(),goodsGained[currGoodIndex]);
        }else if(Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() == smugglersPower){
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    System.out.println("12");
                    return cardEffect();
                }else{
                    cardState = CardState.REMOVE_GOODS;
                    indexOfCurrentTrucker = 0;
                    System.out.println("13");
                    return removeGoods();
                }
            }else{
                indexOfCurrentTrucker++;
                System.out.println("14");
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, ParserState.ACTIVATE_CANNONS, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }

        }else {
            defeatedTruckers.add(Game.getIstance().getTrucker(indexOfCurrentTrucker));
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
                cardState = CardState.REMOVE_GOODS;
                indexOfCurrentTrucker = 0;
                System.out.println("15");
                return removeGoods();
            }else{
                indexOfCurrentTrucker++;
                System.out.println("16");
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, ParserState.ACTIVATE_CANNONS, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }
        }
    }

    @Override
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
    public ServerEvent setGoodsCoordinates(Boolean answer, int[] goodsCoordinates, int value) throws GeneralError, InvalidCoordinates, InvalidPositionException {
        if (cardState != CardState.GOODSTOADD) {
            throw new GeneralError("Card state is not GOODSTOADD");
        }

        if(!answer){
            cardState = CardState.REARRANGE;
            return new RearrangeEvent(true, this.winnerTrucker.getNickname());
        }else {
            if (!winnerTrucker.getSpaceShip().checkLoad(goodsCoordinates)) { //check load deve controllare il singolo componente
                throw new InvalidCoordinates("Not goods deposit");
            }
            try {
                // addGood fa anche la remove dalla bank della merce corrente
                Goods goodToAdd = goodsGained[currGoodIndex];
                goodToAdd.setX(goodsCoordinates[0]);
                goodToAdd.setY(goodsCoordinates[1]);
                winnerTrucker.getSpaceShip().addGood(goodToAdd, goodsCoordinates, value, Game.getIstance().getBank());
            } catch (InvalidPositionException e) {
                cardState = CardState.GOODSTOADD;
                throw e;
            }
            if (currGoodIndex == goodsGained.length - 1) { // ci va il -1 qui!!!
                cardState = CardState.REARRANGE;
                return new RearrangeEvent(true, this.winnerTrucker.getNickname());
            } else {
                currGoodIndex++;
                //TODO: Check che non ci sia loop infinito
                do {
                    if (Game.getIstance().getBank().getGoodsInBank(goodsGained[currGoodIndex].getValue()) >= 1) {
                        cardState = CardState.GOODSTOADD;
                        return new PlotGood(true, winnerTrucker.getNickname(),winnerTrucker.getSpaceShip(), goodsGained[currGoodIndex]);
                    } else {
                        currGoodIndex++;
                        if (currGoodIndex == goodsGained.length) {
                            cardState = CardState.REARRANGE;
                            return new RearrangeEvent(true, this.winnerTrucker.getNickname());
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
                return new RearrangeEvent(true, this.winnerTrucker.getNickname());
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

    @Override
    public ServerEvent removeGoods() throws GeneralError{
        if(cardState != CardState.REMOVE_GOODS){
            throw new GeneralError("plotGoodsToRemove not callable in the state " + cardState);
        }

        if(defeatedTruckers.isEmpty()){
            System.out.println("Entering cause defeatedtruckers is empty");
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }

        ArrayList<Goods> goodsList;
        goodsList = defeatedTruckers.getFirst().getSpaceShip().getGoodsList();

        while (colorOfCurrGoodToRemove > 0 && goodsLostCurrTrucker > 0) {
            switch (colorOfCurrGoodToRemove) {
                case 4 -> {
                    numGoodCurrType = defeatedTruckers.getFirst().getSpaceShip().getRedGoods();
                    colorOfCurrGoodToRemove--;
                }
                case 3 -> {
                    numGoodCurrType = defeatedTruckers.getFirst().getSpaceShip().getYellowGoods();
                    colorOfCurrGoodToRemove--;
                }
                case 2 -> {
                    numGoodCurrType = defeatedTruckers.getFirst().getSpaceShip().getGreenGoods();
                    colorOfCurrGoodToRemove--;
                }
                case 1 -> {
                    numGoodCurrType = defeatedTruckers.getFirst().getSpaceShip().getBlueGoods();
                    colorOfCurrGoodToRemove--;
                }
            }

            if (numGoodCurrType > 0 && numGoodCurrType <= goodsLostCurrTrucker) {
                return removeWithoutAsking(goodsList);
            }else if (numGoodCurrType > 0){
                cardState = CardState.REMOVE_ASKING;
                ArrayList<Goods> listOfGoods = defeatedTruckers.getFirst().getSpaceShip().getGoodsList();
                for (int i = 0; i < numGoodCurrType; i++) {
                    goodsToRemove.add(listOfGoods.getFirst());
                }
                return new RemoveGoods(true, defeatedTruckers.getFirst().getNickname(), defeatedTruckers.getFirst().getSpaceShip(),goodsToRemove);
            }
        }

        // se sei uscito perche non avevi merci devo controllare se puoi perdere batterie
        if(colorOfCurrGoodToRemove == 0 && goodsLostCurrTrucker > 0){
            if(defeatedTruckers.getFirst().getSpaceShip().getNumBatteries() == 0){

                SpaceShip spaceShip = defeatedTruckers.getFirst().getSpaceShip();
                String nickname = defeatedTruckers.getFirst().getNickname();

                defeatedTruckers.removeFirst();
                System.out.println("Removed " + nickname + " from " + spaceShip);


                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    return new RemotionConfirmed(ServerEventType.GOODS_REMOVED, nickname, spaceShip);
                }
            }else{
                this.cardState = CardState.REMOVE_BATTERIES;
                return new RemoveBatteries(true, defeatedTruckers.getFirst().getNickname(),defeatedTruckers.getFirst().getSpaceShip(), goodsLostCurrTrucker);
            }
        }else{
            System.out.println(colorOfCurrGoodToRemove);
            System.out.println(goodsLostCurrTrucker);

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
        defeatedTruckers.getFirst().getSpaceShip().removeGoods(list);

        if (goodsLostCurrTrucker == 0 || (goodsLostCurrTrucker > 0 && defeatedTruckers.getFirst().getSpaceShip().getNumBatteries() == 0)) {
            // se questo trucker ha perso tutte le merci notifico che la rimozione è stata fatta e passo al prossimo
            // per passare al prossimo obbligo il corrente a mandarmi un ok/continue

            SpaceShip spaceShip = defeatedTruckers.getFirst().getSpaceShip();
            String nickname = defeatedTruckers.getFirst().getNickname();

            defeatedTruckers.removeFirst();
            System.out.println("Removed " + nickname + " from " + spaceShip);

            cardState = CardState.REMOVE_GOODS;
            return new RemotionConfirmed(ServerEventType.GOODS_REMOVED,nickname,spaceShip);
        } else {
            // il trucker corrente deve perdere batterie
            cardState = CardState.REMOVE_BATTERIES;
            return new RemoveBatteries(true, defeatedTruckers.getFirst().getNickname(),defeatedTruckers.getFirst().getSpaceShip(), goodsLostCurrTrucker);
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
     */
    @Override
    public ServerEvent removeBatteries(ArrayList<int[]> coordinatesToRemoveBatteries) throws GeneralError,InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries {
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
        if(counterOfBatteriesSelectedByPlayer != goodsLostCurrTrucker){
            throw new WrongNumberOfBatteriesSelected("The total number of batteries you selected to remove is not " + goodsLostCurrTrucker);
        }
        try{
            defeatedTruckers.getFirst().getSpaceShip().removeBatteries(coordinatesToRemoveBatteries);
        }catch(NotEnoughBatteries e){
            throw new NotEnoughBatteries("There is a battery storage that has less batteries then the number of batteries you wanted to remove in that one");
        }

        SpaceShip spaceShip = defeatedTruckers.getFirst().getSpaceShip();
        String nickname = defeatedTruckers.getFirst().getNickname();

        defeatedTruckers.removeFirst();
        System.out.println("Removed " + nickname + " from " + spaceShip);

        cardState = CardState.REMOVE_GOODS;
        return new RemotionConfirmed(ServerEventType.GOODS_REMOVED,nickname,spaceShip);
    }


    /**
     * Applies the changes provided by the card
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError{
        if(cardState != CardState.ESECUZIONE){
            throw new GeneralError("Can't call cardEffect in the state " + cardState);
        }
        if(winDecision && winnerTrucker!=null){
            winDecision = false;
            Game.getIstance().moveBackward(winnerTrucker, days);
        }
        ArrayList<String> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();
        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }
        GamePersistence.saveGame(GameController.getIstance());
        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track);
    }

    public void showShipLayout(String nickname, Component[][] layout) {
        System.out.println("\nShip layout of " + nickname + ":\n");

        // Calcolo dinamico celle valide
        boolean[][] validCells = new boolean[11][12];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Component c = layout[i][j];
                if(c == null || !c.getType().equals("Empty Space")) {
                    validCells[i][j] = true;
                } else {
                    validCells[i][j] = false;
                }
            }
        }

        // Intestazione colonne (sopra)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        for (int i = 5; i < 10; i++) {
            String rowStr = String.format("%2d", i);

            // Riga superiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? " _______ " : "         ");
            }
            System.out.println();

            // Riga link sopra
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[0]);
            }
            System.out.println();

            // Riga centrale con numero riga sinistra
            System.out.print("  " + rowStr + "   ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else if (c == null) System.out.print("|       |");
                else System.out.printf("| %d %c %d |", c.getLink()[3], c.getType().toUpperCase().charAt(0), c.getLink()[1]);
            }
            System.out.println("   " + rowStr);

            // Riga link sotto
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[2]);
            }
            System.out.println();

            // Riga inferiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? "|_______|" : "         ");
            }
            System.out.println();
        }

        // Intestazione colonne (sotto)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        // Dettagli componenti
        System.out.println("\nComponents details:");
        for (int i = 5; i < 10; i++) {
            for (int j = 4; j < 11; j++) {
                if (!validCells[i][j]) continue;
                Component c = layout[i][j];
                if (c == null) continue;

                StringBuilder details = new StringBuilder();
                switch (c) {
                    case Engine engine -> {
                        if (engine.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE ENGINE");
                        else
                            details.append("[").append(i).append(",").append(j).append("] ENGINE");
                    }
                    case Cannon cannon -> {
                        if (cannon.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                        else
                            details.append("[").append(i).append(",").append(j).append("] CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                    }
                    case Load load -> {
                        if (load.getIsSpecial()) {
                            details.append("[").append(i).append(",").append(j).append("] SPECIAL LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        } else {
                            details.append("[").append(i).append(",").append(j).append("] LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        }
                    }
                    case Shield shield -> {
                        details.append("[").append(i).append(",").append(j).append("] SHIELD (direction: ").append(shield.getDirection()).append(")");
                    }
                    case BatteryStorage battery -> {
                        details.append("[").append(i).append(",").append(j).append("] BATTERY STORAGE (Capacity: ").append(battery.getCapacity()).append(")");
                        if(battery.getQuantity() != 0) {
                            details.append(" contains ").append(battery.getQuantity()).append(battery.getQuantity() == 1 ? " battery" : " batteries");
                        } else {
                            details.append(" is empty");
                        }
                    }
                    case Cabin cabin -> {
                        details.append("[").append(i).append(",").append(j).append("] CABIN");
                        if(cabin.getIsMain()){
                            System.out.println(cabin.getColor());
                        }
                        if(cabin.getQuantity() != 0){
                            if (cabin.getAlien() != null) {
                                details.append(cabin.getQuantity()).append(" contains 1 " + cabin.getAlien().getColor() + " alien");
                            } else {
                                details.append(" contains ").append(cabin.getQuantity()).append(cabin.getQuantity() == 1 ? " human" : " humans");
                            }
                        } else {
                            details.append(" is empty");
                        }

                    }
                    case LifeSupport lifeSupport -> {
                        details.append("[").append(i).append(",").append(j).append("] ").append("LIFE SUPPORT is ").append(lifeSupport.getColor());
                    }
                    default -> {
                        details.append("[").append(i).append(",").append(j).append("] ").append(c.getType().toUpperCase());
                    }
                }
                System.out.println(details);
            }
        }
    }

}
