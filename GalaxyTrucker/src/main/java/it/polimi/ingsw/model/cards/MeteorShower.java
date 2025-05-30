package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private final ArrayList<Boolean> answers;
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
        this.answers = new ArrayList<>();
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
        if(state != CardState.ROLL_DICE) {
            throw new GeneralError("Non si può girare i dadi nello stato " + this.state);
        }
        if(currMeteorIndex <0 || currMeteorIndex >= meteors.size()){
            throw new GeneralError("Stai cercando di accedere a un meteorite che non esiste, la carta dovrebbe essere finita");
        }
        meteor = meteors.get(currMeteorIndex);
        currMeteorIndex++;
        this.roll = Game.getIstance().rollDice();
        if (meteor[0]==1) {
            state=CardState.ACTIVATE_CANNONS;
            return new RollDiceResult("Meteorite grande, attivare Cannoni doppi?", currMeteorIndex, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true);
        }else{
            state=CardState.ACTIVATE_SHIELDS;
            return new RollDiceResult("Meteorite piccolo, attivare Scudo?", currMeteorIndex, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true);
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
        if (this.state != CardState.ESECUZIONE) {
            throw new GeneralError("Non è possibile eseguire card effect nello stato " + this.state);
        }

        List<Trucker> truckers = Game.getIstance().getTruckerList();
        if(answers.size() != truckers.size()){
            throw new GeneralError("Non ho una risposta per ogni trucker");
        }
        boolean isBig = (meteor[0] == 1);
        int direction = meteor[1];
        int j = 0;
        for (Trucker trucker : truckers) {
            SpaceShip ship = trucker.getSpaceShip();
            if (ship.isHit(roll, direction)) {
                if (isBig) {
                    // qui assumo che il meteorite grande possa essere distrutto da un cannone ma è da rivedere
                    if (!ship.shootMeteor(direction, roll)) {
                        ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                    }
                } else {
                    // Un meteorite piccolo può rimbalzare o essere fermato dallo scudo
                    if (!ship.blockSmallMeteor(direction, roll, answers.get(j))){
                        ship.destroyComponent(roll, direction, Game.getIstance().getBank());
                    }
                }
            }
            trucker.getSpaceShip().deActivateShields();
            trucker.getSpaceShip().deactivateDoubleCannons();
            j++;
        }
        answers.clear();
        if (meteors.size()==(currMeteorIndex)) {
            ArrayList<String> truckerRemoved;
            truckerRemoved = Game.getIstance().endTurn();
            return new CardFinished(false, Game.getIstance().getTruckerList(), truckerRemoved);
        }else {
            this.state = CardState.ROLL_DICE;
            return new BooleanDecision(ServerEventType.ROLL_DICE, false, ParserState.ROLL_DICE, "Roll the dice", Game.getIstance().getTruckerList().getFirst().getNickname());
        }
    }

}

