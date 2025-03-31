package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Trucker;
import it.polimi.ingsw.Model.Visitor.CardVisitor;

import java.util.ArrayList;
import java.util.List;


/**
 * In order of position, starting from the leader, each trucker decides which double engine wants to activate, using batteries, for the calculation of the total engine power.
 * The total engine power of the trucker is the number of days of flight that his/her spaceship will gain on the dashboard
 */
public class OpenSpace extends Card {
    private ArrayList<ArrayList<int[]>> truckersCoordinates = new ArrayList<>();
    private CardState state;
    //lista che contiene per ogni trucker la lista di coordinates dei motori doppi che vuole attivare
    //la lista esterna associa ogni puntatore a un trucker, quindi la lunghezza della lista esterna è quella di truckers.
    //mantiene l'ordine dei truckers nel fare questa associazione

    /**
     * Open space constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public OpenSpace(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Open Space", "Empty vastness of space", trialFlight, level, frontImage, backImage);
    }

    @Override
    public void accept(CardVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Sets for each trucker the coordinates of the double cannon that he/she wants to activate
     * @param truckerCoordinate list of arrays of coordinates
     * @param numTruckers number of truckers
     */
    public void setCoordinates(ArrayList<int[]> truckerCoordinate, int numTruckers) {
        if (this.state != CardState.ATTESA_RISPOSTE) {
            throw new IllegalStateException("Non è possibile inserire le informazioni nello stato " + this.state);
        }
        this.truckersCoordinates.addLast(truckerCoordinate);
        if (truckersCoordinates != null && truckersCoordinates.size() == numTruckers) {
            boolean allValid = true;
            for (ArrayList<int[]> list : truckersCoordinates) {
                if (list == null || list.isEmpty()) {
                    allValid = false;
                    break;
                }
            }
            if (allValid) {
                this.state = CardState.ESECUZIONE;
            }
        }
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
        int[] enginePowerPerTrucker = new int[truckers.size()];
        int i = 0;
        for (Trucker trucker : truckers) {
            enginePowerPerTrucker[i] = trucker.getSpaceShip().getTotalEnginePower(truckersCoordinates.get(i));
            if(enginePowerPerTrucker[i] == 0){
                game.withdraw(trucker);
            }
            i++;
        }
        for (i = 0; i < enginePowerPerTrucker.length; i++) {
            game.moveForward(truckers.get(i), enginePowerPerTrucker[i]);
        }
        state = CardState.ATTESA_RISPOSTE;
    }
}

