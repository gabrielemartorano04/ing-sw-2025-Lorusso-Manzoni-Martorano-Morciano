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
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

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

    /**
     * cosmic credits that can be won
     */
    private final int credits;

    /**
     * flight days to lose if the winner player decides to take credits
     */
    private final int days;

    /**
     * cannonPower of the pirates
     */
    private final int piratesPower;

    /**
     * current state of the card, helper for debug and checking consistency
     */
    private CardState cardState;

    /**
     * A list of shots for this specific Pirates card.
     * Each element represents a shot, where:
     * - index 0 indicates the shot size (1 = large, 0 = small)
     * - index 1 indicates the direction from which the shot comes
     *   (0 = top, 1 = right, 2 = bottom, 3 = left)
     */
    private final ArrayList<int[]> shoots;

    /**
     * index of the current shoot
     */
    private int currShootIndex;

    /**
     * current shoot
     */
    private int[] shoot;

    /**
     * roll dice result for the current shoot
     */
    private int roll;

    /**
     * index of the current player to handle the fact that clients have to respect a specific turn
     */
    private int indexOfCurrentTrucker;

    /**
     * player that has defeated the pirates
     */
    private Trucker winnerTrucker;

    /**
     * list of all defeated players
     */
    private final ArrayList<Trucker> defeatedTruckers;

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
    public Pirates(boolean trialFlight, int level, int credits, int days, int piratesPower, ArrayList<int[]> shoots ,String frontImage, String backImage, int id) {
        super("Pirates", "Dangerous space raiders", trialFlight, level, frontImage, backImage, id);
        this.credits = credits;
        this.days = days;
        this.piratesPower = piratesPower;
        this.shoots = shoots;
        this.cardState = CardState.ACTIVATE_CANNONS;
        this.roll = 0;
        this.indexOfCurrentTrucker = 0;
        this.defeatedTruckers = new ArrayList<>();
        this.currShootIndex = 0;
        this.winnerTrucker = null;
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

        if (power > piratesPower) {
            return handleVictory();
        }else if(power == piratesPower){
            return handleDraw();
        }else {
            return handleDefeat();
        }
    }

    /**
     * handle the victory of the fight
     *
     * @return serverEvent to send to clients
     */
    private ServerEvent handleVictory() {
        winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
        cardState = CardState.COSMIC_CREDITS;
        return new GainCosmicCredits(true, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), credits, days);
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
                cardState = CardState.ROLL_DICE;
                indexOfCurrentTrucker = 0;
                return new BooleanDecision(true, ParserState.ROLL_DICE, defeatedTruckers.getFirst().getNickname(), Game.getIstance().getTruckerList());
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
    private ServerEvent handleDefeat() {
        defeatedTruckers.add(Game.getIstance().getTrucker(indexOfCurrentTrucker));
        if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
            cardState = CardState.ROLL_DICE;
            indexOfCurrentTrucker = 0;
            return new BooleanDecision(true, ParserState.ROLL_DICE, defeatedTruckers.getFirst().getNickname(), Game.getIstance().getTruckerList());
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
     * sets the decision taken by the winner if there is one
     *
     * @param answer: decision taken
     * @return serverEvent to send to clients
     * @throws GeneralError: not in the right state
     */
    @Override
    public synchronized ServerEvent setWinDecision(boolean answer)throws GeneralError{
        if(cardState!=CardState.COSMIC_CREDITS){
            throw new GeneralError("Card state is not COSMIC_CREDITS");
        }
        winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);

        if(answer){
            winnerTrucker.addCosmicCredit(credits);
            Game.getIstance().moveBackward(winnerTrucker, days);
        }

        if(defeatedTruckers.isEmpty()){
            cardState = CardState.ESECUZIONE;
            return cardEffect();
        }
        else{
            indexOfCurrentTrucker = 0;
            cardState = CardState.ROLL_DICE;
            return new BooleanDecision(true, ParserState.ROLL_DICE, defeatedTruckers.getFirst().getNickname(), Game.getIstance().getTruckerList());
        }
    }

    /**
     * roll the dices for the current shot
     *
     * @return the event containing the roll dice result and the current meteor
     */
    @Override
    public synchronized ServerEvent rollDice() throws GeneralError {
        if(cardState != CardState.ROLL_DICE) {
            throw new GeneralError("Non si può girare i dadi nello stato " + cardState);
        }

        if(currShootIndex <0 || currShootIndex >= shoots.size()){
            throw new GeneralError("Stai cercando di accedere a un meteorite che non esiste, la carta dovrebbe essere finita");
        }
        shoot = shoots.get(currShootIndex);
        this.roll = Game.getIstance().rollDice();
        currShootIndex++;

        if(shoot[0] == 1){
            cardState = CardState.ESECUZIONE;
            return new RollDiceResult(ParserState.DISPLAY_BIG_SHOOT, currShootIndex - 1, shoot, null, roll, defeatedTruckers.getFirst().getNickname(), true, Game.getIstance().getTruckerList());

        }else{
            cardState = CardState.ACTIVATE_SHIELDS;
            return new RollDiceResult(ParserState.ACTIVATE_SHIELD,currShootIndex - 1,shoot,null, roll, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), true, Game.getIstance().getTruckerList());
        }
    }

    /**
     * Activates for each player the shield indicated
     *
     * @param answer: if true the current player wants to activate shield otherwise no
     * @param shieldCoord: coordinates of the shield the current player wants to activate
     * @param batteryCoord: coordinates of the battery storage from which remove a battery
     * @return serverEvent to send to clients
     * @throws GeneralError: card is not in the correct state (for debug)
     * @throws InvalidCoordinates: coordinates don't match the type of component in the shield
     * @throws NotEnoughBatteries: not enough batteries to remove were indicated by the plauer
     */
    @Override
    public synchronized ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, InvalidCoordinates, NotEnoughBatteries {
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
            try {
                Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().activateShield(shieldCoord, batteryCoord);
            } catch (NotEnoughBatteries e) {
                throw new NotEnoughBatteries(e.getMessage());
            }
        }
        if(indexOfCurrentTrucker == defeatedTruckers.size() - 1){
            cardState = CardState.ESECUZIONE;
            indexOfCurrentTrucker = 0;
            return cardEffect();
        }
        indexOfCurrentTrucker++;
        return new RollDiceResult(ParserState.ACTIVATE_SHIELD, currShootIndex,shoot,null, roll, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), true, Game.getIstance().getTruckerList());
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
     * Applies the changes provided by the card:
     * shoot the current shot at defeteadtruckers' ships and add cosmic credits to the winner
     *
     * @return serverEvent to send to clients
     */

    @Override
    public synchronized ServerEvent cardEffect() throws GeneralError {

        if (cardState != CardState.ESECUZIONE) {
            throw new GeneralError("Card state is not ESECUZIONE");
        }

        if(defeatedTruckers.isEmpty()){
            ArrayList<Trucker> truckerRemoved;
            truckerRemoved = Game.getIstance().endTurn();

            deactivateTruckersSpaceShip();

            GamePersistence.saveGame(GameController.getIstance());

            return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved,Game.getIstance().getTrack());
        }
        else {
            boolean isBig = (shoot[0] == 1);
            int direction = shoot[1];
            for (Trucker trucker : defeatedTruckers) {
                SpaceShip ship = trucker.getSpaceShip();
                if (ship.isHit(direction,roll)) {
                    if (isBig) {

                        ship.destroyComponent(roll, direction, Game.getIstance().getBank());

                    } else {

                        if (!ship.blockLightCannonShot(direction)) {
                            ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                        }
                    }
                }
                trucker.getSpaceShip().deactivateDoubleCannons();
                trucker.getSpaceShip().deActivateShields();
            }
        }

        if (shoots.size()==(currShootIndex)) {
            ArrayList<Trucker> truckerRemoved;
            truckerRemoved = Game.getIstance().endTurn();

            deactivateTruckersSpaceShip();

            GamePersistence.saveGame(GameController.getIstance());

            return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved,Game.getIstance().getTrack());
        }
        else {
            if (!defeatedTruckers.isEmpty()) {
                this.cardState = CardState.ROLL_DICE;
                indexOfCurrentTrucker = 0;

                return new BooleanDecision(true, ParserState.ROLL_DICE, defeatedTruckers.getFirst().getNickname(), Game.getIstance().getTruckerList());
            }
            else {
                ArrayList<Trucker> truckerRemoved;
                truckerRemoved = Game.getIstance().endTurn();

                deactivateTruckersSpaceShip();

                GamePersistence.saveGame(GameController.getIstance());

                return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved,Game.getIstance().getTrack());
            }
        }
    }

    public void setState(CardState cardState) {
        this.cardState = cardState;
    }

    public void setCurrShootIndex(int currShootIndex) {
        this.currShootIndex = currShootIndex;
    }

    public void setIndexOfCurrentTrucker(int indexOfCurrentTrucker) {
        this.indexOfCurrentTrucker = indexOfCurrentTrucker;
    }

    public void setDefeatedTrucker(Trucker defeatedTrucker) {
        defeatedTruckers.add(defeatedTrucker);
    }

    public void deleteDefeated() {
        defeatedTruckers.clear();
    }

    public int getIndexOfCurrentTrucker() {
        return indexOfCurrentTrucker;
    }
}
