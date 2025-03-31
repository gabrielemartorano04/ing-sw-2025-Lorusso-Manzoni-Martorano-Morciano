package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;
import it.polimi.ingsw.Model.Visitor.CardVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages the single card meteor shower
 * In the order of representation of the meteors on the card, the leader roll the dices for each meteor to determinate the row or column in which the meteor will hit.
 * If it's a small meteor, the trucker doesn't have to worry if he/she has a well assembled ship, otherwise can use a battery to activate a shield oriented in that direction to protect the ship.
 * If it's a big meteor, the trucker can use, or activate if is double, a cannon to destroy the meteor; a cannon protects only one column or 3 row (his own and the 2 adjacent ones) if rotated;
 */
public class MeteorShower extends Card {
    public CardState state = CardState.PLOT_METEOR;
    // variabile utilizzata per tenere conto delle decisioni prese dai giocatori a ogni aggiornamento dello stato del model
    // dopo la comunicazione del meteorite corrente alla view
    private boolean[] answers;
    // usata per salvare il meteorite che sto considerando in questo momento senza chiamare getCurrentMeteor perché altrimenti
    // sposterei avanti di uno la i
    private int[] meteor;
    private int roll;

    // contatore per tenere traccia del meteorite corrente
    private int i = 0;
    private final ArrayList<int[]> direction;
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
        this.direction = direction;
    }

    @Override
    public void accept(CardVisitor visitor) {
        visitor.visit(this);
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
    public ArrayList<int[]> getDirection() {
        return direction;
    }

    public int[] getCurrentMeteor(){
        if(state != CardState.PLOT_METEOR) {
            throw new IllegalStateException("Non si può settare le decisioni nello stato" + this.state);
        }
        if(i<0 || i >= direction.size()){
            throw new RuntimeException("Stai cercando di accedere a un meteorite che non esiste");
        }
        int[] meteor = direction.get(i);
        this.meteor = meteor;
        i++;
        state = CardState.ROLL_DICE;
        return meteor;
    }

    public void rollDice(Game game){
        if(state != CardState.ROLL_DICE){
            throw new IllegalStateException("Non si può girare i dadi nello stato " + this.state);
        }
        this.roll = game.rollDice();
        state = CardState.ATTESA_RISPOSTE;
    }

    public void setAnswers(boolean[] answers) {
        if(state != CardState.ATTESA_RISPOSTE){
            throw new IllegalStateException("Non si può settare le decisioni nello stato" + this.state);
        }
        this.answers = answers;
        state = CardState.ESECUZIONE;
    }


    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) {
        if (this.state != CardState.ESECUZIONE) {
            throw new IllegalStateException("Non è possibile eseguire card effect nello stato " + this.state);
        }

        List<Trucker> truckers = game.getTruckerList();
        if(answers.length != truckers.size()){
            throw new RuntimeException("Non ho una risposta per ogni trucker");
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
                        ship.destroyComponent(roll, direction);
                    }
                } else {
                    // Un meteorite piccolo può rimbalzare o essere fermato dallo scudo
                    if (!ship.blockSmallMeteor(direction, roll, answers[j])) {
                        ship.destroyComponent(roll, direction);
                    }
                }
            }
            j++;
        }
        //per ora non c'è perché è tutto nel Card Controller
        this.state = CardState.PLOT_METEOR;
    }
}

