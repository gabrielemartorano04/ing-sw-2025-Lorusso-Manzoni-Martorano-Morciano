package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that manages the single card pirates
 * In order of position, starting from the leader, each trucker decides which double cannon wants to activate, using batteries, for the calculation of the cannon power.
 * The first trucker that has a cannon power which is higher than the pirates one, defeat them and decide if he/she wants, losing days of flight, to gain cosmic credits.
 * If the trucker's cannon power is equal to the pirates' one, nothing happens to the trucker, but the pirates continue to attack
 * For all the defeated, the first defeated trucker roll the dices for each shoot, in order of representation on the card, to determinate the row or column in which the shoot will hit.
 * If it's a small shoot, the trucker can use a battery to activate a shield oriented in that direction to protect the ship.
 * If it's a big shoot, there is no way possible for the trucker to protect his/her ship.
 */
public class Pirates extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int credits;
    private final int days;
    private final int piratesPower;
    private CardState cardState;

    // shoots è una lista dove shoots.get(i) rappresenta l'i-esimo colpo in questa specifica carta Pirates
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da sx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso
    private final ArrayList<int[]> shoots;

    private int indexOfCurrentTrucker;
    private Trucker winnerTrucker = null; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private final ArrayList<Trucker> defeatedTruckers;
    private boolean winDecision;

    private int currShootIndex;
    private int[] shoot;
    private int roll;

    /**
     * Pirates constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param credits number of cosmic credit to gain
     * @param days number of days of flight to lose
     * @param piratesPower cannon power required
     * @param shoots list of meteor to protect from
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Pirates(boolean trialFlight, int level, int credits, int days, int piratesPower, ArrayList<int[]> shoots ,String frontImage, String backImage) {
        super("Pirates", "Dangerous space raiders", trialFlight, level, frontImage, backImage);
        this.credits = credits;
        this.days = days;
        this.piratesPower = piratesPower;
        this.shoots = shoots;
        this.cardState = CardState.ACTIVATE_CANNONS;
        this.roll = 0;
        this.indexOfCurrentTrucker = 0;
        this.defeatedTruckers = new ArrayList<>();
        this.currShootIndex = 0;
        this.winDecision = false;
    }

    /**
     * Returns the number of cosmic credits to gain
     * @return credit to gain
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Returns the number of days of flight to lose
     * @return days to lose
     */
    public int getDays() {
        return days;
    }

    /**
     * Returns the cannon power required
     * @return cannon power required
     */
    public int getPiratesPower() {
        return piratesPower;
    }

    public CardState getCardState() {return cardState;}
    /**
     * Returns the list of shoots with direction to protect from
     * @return list of shoots to protect from
     */
    public ArrayList<int[]> getShoots() {
        return shoots;
    }


    @Override
    public ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Choose if you want to activate the double cannons. If yes type the coordinates of the cannons then 'battery' and battery storage(s) coordinates . Type 'y, <x1> <y1>, <x2> <y2>,..., battery, <xs1> <ys1> - <numofbatteries>,...' or 'n': ",
                days,
                credits,
                -1,
                null,
                null,
                piratesPower,
                -1,
                -1,
                shoots,
                null,
                null,
                null,
                ParserState.ACTIVATE_CANNONS

        );
    }

    private ServerEvent fightIsWin() throws GeneralError {
        if (Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() > piratesPower) {
            winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
            cardState = CardState.COSMIC_CREDITS;
            return new GainCosmicCredits(true, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), credits, days);
        }else if(Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() == piratesPower){
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    cardState = CardState.ROLL_DICE;
                    indexOfCurrentTrucker = 0;
                    return new BooleanDecision(ServerEventType.ROLL_DICE, false, ParserState.ROLL_DICE, "Roll the dice", defeatedTruckers.getFirst().getNickname());
                }
            }else{
                indexOfCurrentTrucker++;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, ParserState.ACTIVATE_CANNONS, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }

        }else {
            defeatedTruckers.add(Game.getIstance().getTrucker(indexOfCurrentTrucker));
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
                cardState = CardState.ROLL_DICE;
                indexOfCurrentTrucker = 0;
                return new BooleanDecision(ServerEventType.ROLL_DICE, false, ParserState.ROLL_DICE, "Roll the dice", defeatedTruckers.getFirst().getNickname());
            }else{
                indexOfCurrentTrucker++;
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

    @Override
    public ServerEvent setWinDecision(boolean answer)throws GeneralError{
        if(cardState!=CardState.COSMIC_CREDITS){
            throw new GeneralError("Card state is not COSMIC_CREDITS");
        }
        winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
        winDecision = answer;
        if(defeatedTruckers.isEmpty()){
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }
        else{
            indexOfCurrentTrucker = 0;
            cardState = CardState.ROLL_DICE;
            return new BooleanDecision(ServerEventType.ROLL_DICE, true, ParserState.ROLL_DICE, "Roll the dice", defeatedTruckers.getFirst().getNickname());
        }
    }

    @Override
    public ServerEvent rollDice() throws GeneralError {
        if(cardState != CardState.ROLL_DICE) {
            throw new GeneralError("Non si può girare i dadi nello stato " + cardState);
        }

        if(currShootIndex <0 || currShootIndex >= shoots.size()){
            throw new GeneralError("Stai cercando di accedere a un meteorite che non esiste, la carta dovrebbe essere finita");
        }
        shoot = shoots.get(currShootIndex);
        currShootIndex++;
        this.roll = Game.getIstance().rollDice();
        if(shoot[0] == 1){
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }else{
            cardState = CardState.ACTIVATE_SHIELDS;
            return new RollDiceResult(ParserState.ACTIVATE_SHIELD,"Choose if you want to activate a shield", currShootIndex - 1, roll, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), true, "smallshot");
        }
    }

    @Override
    public ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, InvalidCoordinates {
        if(cardState != CardState.ACTIVATE_SHIELDS){
            throw new GeneralError("Non si può settare le decisioni nello stato" + this.cardState);
        }

        if(answer){
            if(!Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().checkShield(shieldCoord)){
                throw new InvalidCoordinates("Not a shield");
            }
            ArrayList<int[]> l = new ArrayList<>();
            l.add(batteryCoord);
            if(!Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().checkBatteryStorage(l)){
                throw new InvalidCoordinates("Not a battery");
            }
            Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().activateShield(shieldCoord, batteryCoord);
        }
        if(indexOfCurrentTrucker == defeatedTruckers.size() - 1){
            cardState = CardState.ESECUZIONE;
            indexOfCurrentTrucker = 0;
            return cardEffect();
        }
        indexOfCurrentTrucker++;
        return new RollDiceResult(ParserState.ACTIVATE_SHIELD,"Do you want to activate a shield?", currShootIndex, roll, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), false, "smallshot");
    }

    /**
     * Applies the changes provided by the card
     */

    @Override
    public ServerEvent cardEffect() throws GeneralError {

        if (cardState != CardState.ESECUZIONE) {
            throw new GeneralError("Card state is not ESECUZIONE");
        }
        if (winnerTrucker != null && winDecision) {
            winDecision = false;
            winnerTrucker.addCosmicCredit(credits);
            Game.getIstance().moveBackward(winnerTrucker, days);
        }

        if (!defeatedTruckers.isEmpty()) {
            int j =0;
            boolean isBig = (shoot[0] == 1);
            int direction = shoot[1];
            for (Trucker trucker : defeatedTruckers) {
                SpaceShip ship = trucker.getSpaceShip();
                if (ship.isHit(roll, direction)) {
                    if (isBig) {
                        // colpo grande non può essere stoppato quindi devo distruggere il componente
                        ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                    } else {
                        // Un colpo piccolo può solo essere fermato con lo scudo
                        if (!ship.blockLightCannonShot(direction)) {
                            ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                        }
                    }
                }
                j++;
            }

        }

        else {
            ArrayList<String> truckerRemoved;
            truckerRemoved = Game.getIstance().endTurn();

            GamePersistence.saveGame(GameController.getIstance());
            return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved,Game.getIstance().getTrack());
        }

        if (shoots.size()==(currShootIndex)) {
            ArrayList<String> truckerRemoved;
            truckerRemoved = Game.getIstance().endTurn();

            GamePersistence.saveGame(GameController.getIstance());
            return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved,Game.getIstance().getTrack());
        }

        else {
            if (!defeatedTruckers.isEmpty()) {
                this.cardState = CardState.ROLL_DICE;
                indexOfCurrentTrucker = 0;
                return new BooleanDecision(ServerEventType.ROLL_DICE, true, ParserState.ROLL_DICE, "Roll the dice", defeatedTruckers.getFirst().getNickname());
            }else {
                ArrayList<String> truckerRemoved;
                truckerRemoved = Game.getIstance().endTurn();
                GamePersistence.saveGame(GameController.getIstance());
                return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved,Game.getIstance().getTrack());
            }
        }
    }

}
