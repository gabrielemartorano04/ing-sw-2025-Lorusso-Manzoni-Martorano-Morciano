package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.visitor.CardVisitor;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that manages the single card slavers
 * In order of position, starting from the leader, each trucker decides which double cannon wants to activate, using batteries, for the calculation of the cannon power.
 * The first trucker that has a cannon power which is higher than the slavers one, defeat them and decide if he/she wants, losing days of flight, to gain cosmic credits.
 * If the trucker's cannon power is equal to the slavers' one, nothing happens to the trucker, but the slavers continue to attack
 * For all the defeated truckers, they have to remove some crew members
 */
public class Slavers extends Card {
    private final int slaversPower;
    private final int credits;
    private final int days;
    private final int astronautsLost;
    private Trucker winnerTrucker = null; // trucker che ha sconfitto la carta e vuole la ricompensa, se è uguale a Null allora nessuno ha accettato la ricompensa
    private ArrayList<Trucker> defeatedTruckers = new ArrayList<>();
    private ArrayList<ArrayList<int[]>> truckersCoordinatesToRemoveHumans;
    // contiene le coordinate delle cabine scelte dall'utente per rimuovere gli umani. Queste coordinate sono organizzate
    // in liste di liste. La lista esterna è una per ogni trucker, la lista interna contiene la lista di coordinate per quel
    // trucker, mantenute nell'ordine dei trucker.
    private CardState cardState;
    private ArrayList<ArrayList<int[]>> doubleCannonsCoord; // coordinates of the double cannons the trucker wants to activate
    private int playersDecisions;// number of players that gave a choice
    private Boolean choiceToGain;

    /**
     * Slavers constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param credits number of cosmic credit to gain
     * @param days number of days of flight to lose
     * @param astronautsLost number of crew member to lose
     * @param slaversPower cannon power required
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public Slavers(boolean trialFlight, int level, int credits, int days, int astronautsLost, int slaversPower, String frontImage, String backImage ) {
        super("Slavers", "Intergalactic slave traders", trialFlight, level, frontImage, backImage);
        this.credits = credits;
        this.days = days;
        this.astronautsLost = astronautsLost;
        this.slaversPower = slaversPower;
        this.doubleCannonsCoord= new ArrayList<>();
        this.cardState = CardState.ATTESA_RISPOSTE;
        this.playersDecisions = 0;
        this.truckersCoordinatesToRemoveHumans = new ArrayList<>();
    }

    @Override
    public void accept(CardVisitor visitor) {
        visitor.visitForSlavers(this);
    }

    /**
     * Returns the cannon power required
     * @return cannon power required
     */
    public int getSlaversPower() {
        return slaversPower;
    }

    /**
     * Returns the number of crew members to lose
     * @return crew members to lose
     */
    public int getAstronautsLost() {
        return astronautsLost;
    }

    /**
     * Returns the number of days of flight to lose
     * @return days to lose
     */
    public int getDays() {
        return days;
    }

    /**
     * Returns the number of cosmic credits to gain
     * @return credit to gain
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the list of defeated truckers
     * @param defeatedTruckers list of truckers ID
     */
    public void setDefeatedTruckers(ArrayList<Trucker> defeatedTruckers) {
        this.defeatedTruckers = defeatedTruckers;
    }

    /**
     * Sets the trucker that defeat the pirates
     * @param winnerTrucker trucker ID
     */
    public void setWinnerTrucker(Trucker winnerTrucker) {
        this.winnerTrucker = winnerTrucker;
    }

    /**
     * Sets, for each trucker, the coordinates of the cabin where he/she want to remove the crew members
     * @param truckersCoordinatesToRemoveHumans list of lists of arrays of coordinates
     */
    public CardState addTruckersCoordinatesToRemoveHumans(ArrayList<int[]> truckersCoordinatesToRemoveHumans, int numPlayer, Trucker trucker) throws InvalidCoordinates {
        if (cardState != CardState.HUMANSTOREMOVE) {
            throw new IllegalStateException("Card state in not HUMANSTOREMOVE");
        }
        if(!defeatedTruckers.isEmpty() || playersDecisions<numPlayer) {
            try {
                trucker.getSpaceShip().checkCabin(truckersCoordinatesToRemoveHumans, astronautsLost);
                this.truckersCoordinatesToRemoveHumans.add(truckersCoordinatesToRemoveHumans);
                playersDecisions++;
            } catch (InvalidCoordinates e) {
                throw new InvalidCoordinates(e.getMessage());
            }
            if (playersDecisions==numPlayer) {
                if(winnerTrucker!=null) {
                    cardState = CardState.CHOOSE_TO_GAIN;
                }else cardState = CardState.ESECUZIONE;
            }
        }else{
            if(winnerTrucker!=null) {
                cardState = CardState.CHOOSE_TO_GAIN;
            }else cardState = CardState.ESECUZIONE;
        }
        return cardState;
    }

    public CardState addDoubleCannonsCoord(ArrayList<int[]> doubleCannonsCoord, int numPlayer, Trucker trucker) throws IllegalStateException {
        if (cardState != CardState.ATTESA_RISPOSTE) {
            throw new IllegalStateException("Card state is not ATTESA_RISPOSTE");
        }
        if (trucker.getSpaceShip().getNumDoubleCannon()!=0) {
            if (!doubleCannonsCoord.isEmpty() || trucker.getSpaceShip().checkDoubleCannon(Objects.requireNonNull(doubleCannonsCoord))) {
                this.doubleCannonsCoord.addLast(doubleCannonsCoord);
                playersDecisions++;
            } else {
                throw new IllegalArgumentException("not a double cannon");
            }
        }
        if (trucker.getSpaceShip().getTotalCannonPower(doubleCannonsCoord) > slaversPower) {
            winnerTrucker = trucker;
            cardState = CardState.HUMANSTOREMOVE;
        }else if(trucker.getSpaceShip().getTotalCannonPower(doubleCannonsCoord) == slaversPower){
            if (playersDecisions == numPlayer) {
                playersDecisions=0;
                cardState = CardState.HUMANSTOREMOVE;
            }else cardState = CardState.ATTESA_RISPOSTE;
        }else {
            defeatedTruckers.add(trucker);
            if (playersDecisions == numPlayer) {
                playersDecisions=0;
                cardState = CardState.HUMANSTOREMOVE;
            }else cardState = CardState.ATTESA_RISPOSTE;
        }
        return cardState;
    }

    public CardState setChooseToGain(Boolean choice) throws IllegalStateException {
        if (cardState != CardState.GAIN) {
            throw new IllegalStateException("Card state is not GAIN");
        }
        this.choiceToGain = choice;
        cardState = CardState.ESECUZIONE;
        return cardState;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public CardState cardEffect(Game game) throws IllegalStateException {
        if (cardState != CardState.ESECUZIONE) {
            throw new IllegalStateException("Card state is not ESECUZIONE");
        }
        int i = 0;
        if(winnerTrucker!=null && choiceToGain){
            winnerTrucker.addCosmicCredit(credits);
            game.moveBackward(winnerTrucker, days);
        }
        if(!defeatedTruckers.isEmpty()) {
            for (Trucker trucker : defeatedTruckers) {
                trucker.getSpaceShip().removeNumHuman(truckersCoordinatesToRemoveHumans.get(i));
                i++;
            }
        }
        cardState=CardState.EXIT;
        return cardState;
    }

}