package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.utilities.exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

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
    private static final long serialVersionUID = 1L;
    private final int credits;
    private final int days;
    private final int piratesPower;
    private final ArrayList<int[]> shoots;
    // shoots è una lista dove shoots.get(i) rappresenta l'i-esimo colpo in questa specifica carta Pirates
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da sx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

    private CardState cardState;
    private int indexOfCurrentTrucker;
    private Trucker winnerTrucker = null; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private ArrayList<Trucker> defeatedTruckers;
    private boolean winDecision;

    private int currShootIndex;
    private int[] shoot;
    private int roll;

    private ArrayList<Boolean> shieldAnswers;


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
        this.cardState = CardState.WAIT_CANNONS;
        this.roll = 0;
        this.indexOfCurrentTrucker = 0;
        this.defeatedTruckers = new ArrayList<>();
        this.currShootIndex = 0;
        this.winDecision = false;
        this.shieldAnswers = new ArrayList<>();
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
                "Do you want to activate your doubleCannons?",
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
                this.getCardState()
        );
    }

    private ServerEvent fightIsWin() throws GeneralError {
        if (Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() > piratesPower) {
            winnerTrucker = Game.getIstance().getTrucker(indexOfCurrentTrucker);
            cardState = CardState.COSMIC_CREDITS;
            return new GainCosmicCredits(true, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), credits);
        }else if(Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().getTotalCannonPower() == piratesPower){
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()) {
                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    cardState = CardState.ROLL_DICE;
                    return new BooleanDecision(ServerEventType.ROLL_DICE, false, this.cardState, "Roll the dice", defeatedTruckers.getFirst().getNickname());
                }
            }else{
                indexOfCurrentTrucker++;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, this.cardState, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }

        }else {
            defeatedTruckers.add(Game.getIstance().getTrucker(indexOfCurrentTrucker));
            if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()) {
                if(defeatedTruckers.isEmpty()){
                    cardState = CardState.ESECUZIONE;
                    return cardEffect();
                }else{
                    cardState = CardState.ROLL_DICE;
                    return new BooleanDecision(ServerEventType.ROLL_DICE, false, this.cardState, "Roll the dice", defeatedTruckers.getFirst().getNickname());
                }
            }else{
                indexOfCurrentTrucker++;
                return new BooleanDecision(ServerEventType.ACTIVATE_CANNONS, true, this.cardState, "Do you want to activate double Cannons?", Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname());
            }
        }
    }

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
            cardState = CardState.ROLL_DICE;
            return new BooleanDecision(ServerEventType.ROLL_DICE, true, this.cardState, "Roll the dice", defeatedTruckers.getFirst().getNickname());
        }
    }

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
            return new RollDice("Choose if you want to activate a shield", this.getCardState(), currShootIndex - 1, roll, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), true);
        }
    }



    public ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, InvalidCoordinates {
        if(cardState != CardState.ACTIVATE_SHIELDS){
            throw new GeneralError("Non si può settare le decisioni nello stato" + this.cardState);
        }
        this.shieldAnswers.addLast(answer);
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
        if(indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()){
            cardState = CardState.ESECUZIONE;
            indexOfCurrentTrucker = 0;
            return cardEffect();
        }
        indexOfCurrentTrucker++;
        return new RollDice("Do you want to activate a shield?", this.getCardState(), currShootIndex, roll, Game.getIstance().getTrucker(indexOfCurrentTrucker).getNickname(), false);
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
                        if (!ship.blockLightCannonShot(direction, roll, shieldAnswers.get(j))) {
                            ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                        }
                    }
                }
                j++;
            }
        }
        if (shoots.size()==(currShootIndex)) {
            this.cardState = CardState.EXIT;
            return new CardFinished(true, this.cardState, Game.getIstance().getTruckerList());
        }else {
            this.cardState = CardState.ROLL_DICE;
            return new BooleanDecision(ServerEventType.ROLL_DICE, true, this.cardState, "Roll the dice", defeatedTruckers.getFirst().getNickname());
        }
    }

}
