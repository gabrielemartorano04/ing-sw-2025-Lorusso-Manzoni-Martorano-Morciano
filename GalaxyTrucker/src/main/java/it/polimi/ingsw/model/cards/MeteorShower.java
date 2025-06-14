package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.events.serverevents.cardevents.RollDiceResult;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that manages the single card meteor shower
 * In the order of representation of the meteors on the card, the leader roll the dices for each meteor to determinate the row or column in which the meteor will hit.
 * If it's a small meteor, the trucker doesn't have to worry if he/she has a well assembled ship, otherwise can use a battery to activate a shield oriented in that direction to protect the ship.
 * If it's a big meteor, the trucker can use, or activate if is double, a cannon to destroy the meteor; a cannon protects only one column or 3 row (his own and the 2 adjacent ones) if rotated;
 */
public class MeteorShower extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public CardState state;
    private int indexOfCurrTrucker;

    private int[] meteor;
    private int roll;

    private int currMeteorIndex = 0;

    // direction è una lista dove direction.get(i) rappresenta l'i-esimo meteorite in questa specifica MeteorShower
    // direction.get(i)[0] vale 1 se il meteorite è grande 0 se è piccolo
    // direction.get(i)[1] indice la direzione da cui arriva il meteorite (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da sx)
    // direction è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso
    private final ArrayList<int[]> meteors;


    /**
     * Meteor shower constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param direction list of directions of the meteors
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public MeteorShower(boolean trialFlight, ArrayList<int[]> direction, int level, String frontImage, String backImage) {
        super("Meteor Shower", "A dangerous meteor storm", trialFlight, level, frontImage, backImage);
        this.meteors = direction;
        this.indexOfCurrTrucker = 0;
        this.state = CardState.ROLL_DICE;
    }


//    public ArrayList<int[]> getMeteors() {
//        return meteors;
//    }

    public CardState getCardState() {
        return state;
    }


    @Override
    public ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Roll the dice",
                -1,
                -1,
                -1,
                null,
                meteors,
                -1,
                -1,
                -1,
                null,
                null,
                null,
                null,
                ParserState.ROLL_DICE
        );
    }

    public ServerEvent rollDice() throws GeneralError {

        System.out.println("");

        if(state != CardState.ROLL_DICE) {
            throw new GeneralError("Can't roll dice in the state " + this.state);
        }
        if(currMeteorIndex <0 || currMeteorIndex >= meteors.size()){
            throw new GeneralError("Card shoulb be already finished");
        }
        meteor = meteors.get(currMeteorIndex);
        currMeteorIndex++;

        int direction = meteor[1];
        String sizeText = (meteor[0] == 1) ? "big" : "small";

        String dirText;
        switch (direction) {
            case 0 -> dirText = "from the top";
            case 1 -> dirText = "from the right side";
            case 2 -> dirText = "from the bottom";
            case 3 -> dirText = "from the left side";
            default -> dirText = "from an unknown direction";
        }
        System.out.println("Meteorite " + currMeteorIndex + " (" + sizeText + ") arriving "  + dirText + " total number of meteors in the card " + meteors.size());

        this.roll = Game.getIstance().rollDice();
        this.roll = 8;
        if (meteor[0]==1) {
            state=CardState.ACTIVATE_CANNONS;
            return new RollDiceResult("Meteorite grande, attivare Cannoni doppi?", currMeteorIndex, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true, "bigmeteor");
        }else{
            state=CardState.ACTIVATE_SHIELDS;
            return new RollDiceResult("Meteorite piccolo, attivare Scudo?", currMeteorIndex, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true, "smallmeteor");
        }
    }

    @Override
    public ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, InvalidCoordinates {
        if (state != CardState.ACTIVATE_SHIELDS) {
            throw new GeneralError("Non si può settare le decisioni nello stato" + this.state);
        }

        System.out.println(Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname() + " has decidec to activate? " + answer);
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

        if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()-1) {
            state = CardState.ESECUZIONE;
            indexOfCurrTrucker = 0;
            return cardEffect();
        }
        indexOfCurrTrucker++;
        return new BooleanDecision(ServerEventType.ACTIVATE_SHIELD, true, ParserState.ACTIVATE_SHIELD, "vuoi attivare uno scudo?", Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
    }

    @Override
    public ServerEvent activateCannons(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws GeneralError, InvalidCoordinates, NotEnoughBatteries {
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

        if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()-1) {
            this.state = CardState.ESECUZIONE;
            indexOfCurrTrucker=0;
            return cardEffect();
        }
        indexOfCurrTrucker++;
        return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, ParserState.ACTIVATE_CANNONS, "vuoi attivare un Cannone Doppio?", Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
    }

    /**
     * Applies the changes provided by the card
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError {
        System.out.println("Entrato in cardEffect");

        if (this.state != CardState.ESECUZIONE) {
            throw new GeneralError("Non è possibile eseguire card effect nello stato " + this.state);
        }

        List<Trucker> truckers = Game.getIstance().getTruckerList();

        boolean isBig = (meteor[0] == 1);
        int direction = meteor[1];
        int j = 0;
        for (Trucker trucker : truckers) {
            SpaceShip ship = trucker.getSpaceShip();

            showShipLayout("io", ship.getComponents());

            if (ship.isHit(direction, roll)) {
                System.out.println("La nave è colpita");
                if (isBig) {
                    System.out.println("Entrato");

                    // qui assumo che il meteorite grande possa essere distrutto da un cannone ma è da rivedere
                    if (!ship.shootMeteor(direction, roll)) {
                        ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                    }
                } else {
                    // Un meteorite piccolo può rimbalzare o essere fermato dallo scudo
                    if (!ship.blockSmallMeteor(direction, roll)){
                        ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                    }
                }
            }else {
                System.out.println("La nave di " + trucker.getNickname() + " non è colpita");
            }
            trucker.getSpaceShip().deActivateShields();
            trucker.getSpaceShip().deactivateDoubleCannons();
            j++;
        }
        if (meteors.size()==(currMeteorIndex)) {
            ArrayList<String> truckerRemoved;
            truckerRemoved = Game.getIstance().endTurn();
            Map<String, Integer> track = new HashMap<>();
            for (Trucker t : Game.getIstance().getTruckerList()) {
                track.put(t.getNickname(), t.getPosition());
            }
            GamePersistence.saveGame(GameController.getIstance());
            return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track);
        }else {
            this.state = CardState.ROLL_DICE;
            return new BooleanDecision(ServerEventType.ROLL_DICE, false, ParserState.ROLL_DICE, "Roll the dice", Game.getIstance().getTruckerList().getFirst().getNickname());
        }
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

