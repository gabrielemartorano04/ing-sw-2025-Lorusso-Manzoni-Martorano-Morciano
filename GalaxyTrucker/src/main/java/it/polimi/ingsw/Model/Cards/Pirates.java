package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.SpaceShip;
import it.polimi.ingsw.Model.Trucker;

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
public class Pirates extends Card {
    private final int credits;
    private final int days;
    private final int piratesPower;
    private Trucker winnerTrucker = null; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private ArrayList<Trucker> defeatedTruckers;
    private final ArrayList<int[]> shoots;
    private CardState cardState;
    private ArrayList<ArrayList<int[]>> doubleCannonsCoord; // coordinates of the double cannons the trucker wants to activate
    private int playersDecisions; // number of players that gave a choice
    private boolean activateShield;
    private boolean choiceToGain;
    private int i;
    private int[] shoot;
    private int roll;
    private boolean [] usesShield;

    // shoots è una lista dove shoots.get(i) rappresenta l'i-esimo colpo in questa specifica carta Pirates
    // shoots.get(i)[0] vale 1 se il colpo è grande 0 se è piccolo
    // shoots.get(i)[1] indice la direzione da cui arriva il colpo (0 = dall'alto, 1 = da dx, 2 = dal basso, 3 = da sx)
    // shoots è già ordinata nell'ordine corretto nel quale vanno considerati i meteoriti secondo le regole del gioco
    // cioè scegliendo il meteorite dall'alto verso il basso

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
        this.doubleCannonsCoord= new ArrayList<>();
        this.cardState = CardState.ATTESA_RISPOSTE;
        this.playersDecisions = 0;
        this.activateShield = false;
        this.choiceToGain = false;
        this.i = 0;
        this.roll = 0;
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

    /**
     * Returns the list of shoots with direction to protect from
     * @return list of shoots to protect from
     */
    public ArrayList<int[]> getShoots() {
        return shoots;
    }

    /**
     * Sets the list of defeated truckers
     * @param defeatedTruckers list of truckers ID
     */
    public void setDefeatedTruckers(ArrayList<Trucker> defeatedTruckers) {
        this.defeatedTruckers = defeatedTruckers;
    }

    public void addDoubleCannonsCoord(ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker) throws IllegalStateException {
        if (cardState != CardState.ATTESA_RISPOSTE) {
            throw new IllegalStateException("Card state is not ATTESA_RISPOSTE");
        }
        this.doubleCannonsCoord.addLast(doubleCannonsCoord);
        playersDecisions++;
        if (trucker.getSpaceShip().getTotalCannonPower(doubleCannonsCoord) > piratesPower) {
            winnerTrucker = trucker;
            cardState = CardState.GAIN;
        }else if(trucker.getSpaceShip().getTotalCannonPower(doubleCannonsCoord) == piratesPower){
            cardState = CardState.ATTESA_RISPOSTE;
        }else {
            defeatedTruckers.add(trucker);
            if (playersDecisions == numPlayer) {
                cardState = CardState.ESECUZIONE;
            }else cardState = CardState.ATTESA_RISPOSTE;
        }
    }

    public void setActivateShield(boolean activateShield) {

    }

    public void setChooseToGain(Boolean choice) throws IllegalStateException {
        if (cardState != CardState.GAIN) {
            throw new IllegalStateException("Card state is not GAIN");
        }
        this.choiceToGain = choice;
        cardState = CardState.ESECUZIONE;
    }

    /**
     * Sets the trucker that defeat the pirates
     * @param winnerTrucker trucker ID
     */
    public void setWinnerTrucker(Trucker winnerTrucker) {
        this.winnerTrucker = winnerTrucker;
    }

    public ArrayList<int[]> getDirections() {
        return shoots;
    }

    public int[] getCurrentMeteor(){
        if(cardState != CardState.PLOT_METEOR) {
            throw new IllegalStateException("Non si può settare le decisioni nello stato" + this.cardState);
        }
        if(i<0 || i >= shoots.size()){
            throw new RuntimeException("Stai cercando di accedere a un colpo che non esiste");
        }
        int[] meteor = shoots.get(i);
        this.shoot = meteor;
        i++;
        cardState = CardState.ROLL_DICE;
        return meteor;
    }

    public void rollDice(Game game){
        if(cardState != CardState.ROLL_DICE){
            throw new IllegalStateException("Non si può girare i dadi nello stato " + this.cardState);
        }
        this.roll = game.rollDice();
        cardState = CardState.CHIEDI_SCUDI;
    }

    public void setAnswers(boolean[] answers) {
        if(cardState != CardState.CHIEDI_SCUDI){
            throw new IllegalStateException("Non si può settare le decisioni nello stato" + this.cardState);
        }
        this.usesShield = answers;
        cardState = CardState.ESECUZIONE;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public void cardEffect(Game game) throws IllegalStateException {
        if (cardState != CardState.ESECUZIONE) {
            throw new IllegalStateException("Card state is not ESECUZIONE");
        }
        if (winnerTrucker != null && choiceToGain) {
            winnerTrucker.addCosmicCredit(credits);
            game.moveBackward(winnerTrucker, days);
        }

        //colpisco le navi solo se c'è qualcuno da colpire
        if (!defeatedTruckers.isEmpty()) {
            int roll = game.rollDice();
            int j =0;
            boolean isBig = (shoot[0] == 1);
            int direction = shoot[1];
            for (Trucker trucker : defeatedTruckers) {
                SpaceShip ship = trucker.getSpaceShip();
                if (ship.isHit(roll, direction)) {
                    if (isBig) {
                        // colpo grande non può essere stoppato quindi devo distruggere il componente
                        ship.destroyComponent(roll, direction);
                    } else {
                        // Un colpo piccolo può solo essere fermato con lo scudo
                        if (!ship.blockLightCannonShot(direction, roll, usesShield[j])) {
                            ship.destroyComponent(roll, direction);
                        }
                    }
                }
                j++;
            }
        }
        this.cardState = CardState.PLOT_METEOR;
    }
}
