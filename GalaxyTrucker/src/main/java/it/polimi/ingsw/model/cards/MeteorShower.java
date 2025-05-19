package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.RollDice;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.NotEnoughBatteries;

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
    private static final long serialVersionUID = 1L;
    public CardState state = CardState.ROLL_DICE;
    // variabile utilizzata per tenere conto delle decisioni prese dai giocatori a ogni aggiornamento dello stato del model
    // dopo la comunicazione del meteorite corrente alla view
    private ArrayList<Boolean> answers;
    // usata per salvare il meteorite che sto considerando in questo momento senza chiamare getCurrentMeteor perché altrimenti
    // sposterei avanti di uno la i
    private int indexOfCurrTrucker;

    private int[] meteor;
    private int roll;

    // contatore per tenere traccia del meteorite corrente
    private int currMeteorIndex = 0;
    private final ArrayList<int[]> meteors;
    // direction è una lista dove direction.get(i) rappresenta l'i-esimo meteorite in questa specifica MeteorShower
    // direction.get(i)[0] vale 1 se il meteorite è grande 0 se è piccolo
    // direction.get(i)[1] indice la direzione da cui arriva il meteorite (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da sx)
    // direction è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso



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
    }

    /**
     * Returns the list of arrays indicating the direction of each meteor.
     * Each position of the array represents a direction:
     * position 0 = top
     * position 1 = right side
     * position 2 = bottom
     * position 3 = left side
     * @return list of direction
     */
    public ArrayList<int[]> getMeteors() {
        return meteors;
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
                this.getCardState()
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
        state = CardState.ATTESA_RISPOSTE;
        return new RollDice(this.getCardState(), currMeteorIndex - 1, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), true);
    }

    public ServerEvent activateShield(boolean answer, int[] shieldCoord , int[] batteryCoord) throws GeneralError, InvalidCoordinates {
        if(state != CardState.ATTESA_RISPOSTE){
            throw new GeneralError("Non si può settare le decisioni nello stato" + this.state);
        }
        if(!Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().checkShield(shieldCoord)){
            throw new InvalidCoordinates("Not a shield");
        }
        ArrayList<int[]> l = new ArrayList<>();
        l.add(batteryCoord);
        if(!Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().checkBatteryStorage(l)){
            throw new InvalidCoordinates("Not a battery");
        }
        this.answers.addLast(answer);
        if(answer){
            Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip().activateShield(shieldCoord, batteryCoord);
        }

        if(indexOfCurrTrucker == Game.getIstance().getTruckerList().size()){
            state = CardState.ESECUZIONE;
            indexOfCurrTrucker = 0;
            return cardEffect();
        }
        indexOfCurrTrucker++;
        return new RollDice(this.getCardState(), currMeteorIndex - 1, roll, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(), false);
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
        if (meteors.size()==(currMeteorIndex)) {
            this.state = CardState.EXIT;
            this.setIsCardFinished(true);
            return new CardFinished(true, this.getCardState(), Game.getIstance().getTruckerList());
        }else {
            this.state = CardState.ROLL_DICE;
            return new BooleanDecision(ServerEventType.ROLL_DICE, false, this.getCardState(), "Roll the dice", Game.getIstance().getTruckerList().getFirst().getNickname());
        }
    }


    public CardState getCardState() {
        return state;
    }

    public void setCardState(CardState cardState) {
        this.state = cardState;
    }
}

