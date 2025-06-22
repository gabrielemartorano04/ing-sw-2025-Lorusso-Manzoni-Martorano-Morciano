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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that manages the single card meteor shower
 * In the order of representation of the meteors on the card, the leader roll the dices for each meteor to determinate the row or column in which the meteor will hit.
 * If it's a small meteor, the trucker doesn't have to worry if he/she has a well assembled ship, otherwise can use a battery to activate a shield oriented in that direction to protect the ship.
 * If it's a big meteor, the trucker can use, or activate if is double, a cannon to destroy the meteor; a cannon protects only one column or 3 row (his own and the 2 adjacent ones) if rotated;
 */
public class MeteorShower extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * current state of the card, helper for debug and checking consistency
     */
    public CardState state;

    /**
     * index of the current player to handle the fact that clients have to respect a specific turn
     */
    private int indexOfCurrTrucker;

    /**
     * currentMeteor that is threatening players' ships
     */
    private int[] meteor;

    /**
     * current roll dice result for the current meteor
     */
    private int roll;

    /**
     * index of the current meteor in the list "meteors"
     */
    private int currMeteorIndex = 0;

    /**
     * Ordered list of incoming meteors, where each element is an array of two integers:
     * [0] = 1 if the meteor is large, 0 if small;
     * [1] = direction of approach (0 = top, 1 = right, 2 = bottom, 3 = left).
     * The list is pre-sorted in the correct order of resolution.
     */
    private final ArrayList<int[]> meteors;


    /**
     * Meteor shower constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param direction list of directions of the meteors
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public MeteorShower(boolean trialFlight, ArrayList<int[]> direction, int level, String frontImage, String backImage, int id) {
        super("Meteor Shower", "A dangerous meteor storm", trialFlight, level, frontImage, backImage, id);
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

    /**
     * @return an event containing all the card details
     */
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
                ParserState.ROLL_DICE,
                this
        );
    }


    /**
     * roll the dices for the current meteor
     *
     * @return the event containing the roll dice result and the current meteor
     * @throws GeneralError
     */
    public ServerEvent rollDice() throws GeneralError {
        if(state != CardState.ROLL_DICE) {
            throw new GeneralError("Can't roll dice in the state " + this.state);
        }
        if(currMeteorIndex <0 || currMeteorIndex >= meteors.size()){
            throw new GeneralError("Card should be already finished");
        }

        this.roll = Game.getIstance().rollDice();

        meteor = meteors.get(currMeteorIndex);
        currMeteorIndex++;

        if (meteor[0]==1) {
            state=CardState.ACTIVATE_CANNONS;
            return new RollDiceResult(ParserState.ACTIVATE_CANNONS, currMeteorIndex,null,meteor, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true, "bigmeteor");
        }else{
            state=CardState.ACTIVATE_SHIELDS;
            return new RollDiceResult(ParserState.ACTIVATE_SHIELD, currMeteorIndex, null,meteor, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true, "smallmeteor");
        }
    }


    /**
     * Activates for each player the shield indicated
     *
     * @param answer: if true the current player wants to activate shield otherwise no
     * @param shieldCoord: coordinates of the shield the current player wants to activate
     * @param batteryCoord: coordinates of the battery storage from which remove a battery
     * @return: cardeffect if every player has decided, otherwise the event for the next player
     * @throws GeneralError: card is not in the correct state (for debug)
     * @throws InvalidCoordinates: coordinates don't match the type of component in the shield
     * @throws NotEnoughBatteries: not enough batteries to remove were indicated by the plauer
     */
    @Override
    public ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, InvalidCoordinates, NotEnoughBatteries {
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
            try {
                Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().activateShield(shieldCoord, batteryCoord);
            } catch (NotEnoughBatteries e) {
                throw new NotEnoughBatteries(e.getMessage());
            }
        }

        if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()-1) {
            state = CardState.ESECUZIONE;
            indexOfCurrTrucker = 0;
            return cardEffect();
        }
        indexOfCurrTrucker++;
        return new BooleanDecision(ServerEventType.ACTIVATE_SHIELD, true, ParserState.ACTIVATE_SHIELD, "vuoi attivare uno scudo?", Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
    }


    /**
     * Sets for each trucker the coordinates of the double cannons that he/she wants to activate
     *
     * @param activate: true if the current player wants to activate any double Cannons, false otherwise
     * @param coord: coordinates of the doubleCannons to activate
     * @param batteryCoord: coordinates of the batteryStorages from which remove the batteries
     * @return: cardEffect if it was the last player, otherwise it updates the next player to let him decide whether
     * to activate any doubleCannons
     * @throws GeneralError: card not in the right state
     * @throws InvalidCoordinates: coordinates do not match to their component type
     * @throws NotEnoughBatteries: not enoughBatteries indicated to activate the doubleCannons selected
     */
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
     * Applies the changes provided by the card:
     * shoots players' ships with the current meteor
     *
     * @return the event containing the status of the game after the effects for clients if it was the last meteor,
     * otherwise the event to notify the leader to roll dices
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

            if (ship.isHit(direction, roll)) {
                if (isBig) {

                    if (!ship.shootMeteor(direction, roll)) {
                        ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                    }
                } else {
                    if (!ship.blockSmallMeteor(direction, roll)){
                        ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                    }
                }
            }

            trucker.getSpaceShip().deActivateShields();
            trucker.getSpaceShip().deactivateDoubleCannons();
            j++;
        }
        if (meteors.size()==(currMeteorIndex)) {
            ArrayList<Trucker> truckerRemoved;
            truckerRemoved = Game.getIstance().endTurn();

            GamePersistence.saveGame(GameController.getIstance());

            return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, Game.getIstance().getTrack());
        }else {
            this.state = CardState.ROLL_DICE;
            return new BooleanDecision(ServerEventType.ROLL_DICE, true, ParserState.ROLL_DICE, "Roll the dice", Game.getIstance().getTruckerList().getFirst().getNickname());
        }
    }



}

