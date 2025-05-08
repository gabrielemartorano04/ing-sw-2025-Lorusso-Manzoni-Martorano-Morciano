package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.callers.OpenSpaceCaller;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.visitor.CardVisitor;
import it.polimi.ingsw.model.visitor.ParametricCardVisitor;
import it.polimi.ingsw.utilities.exceptions.GeneralError;

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
        super("Open Space", "Empty vastness of space", trialFlight, level, frontImage, backImage, new OpenSpaceCaller());
    }

    /**
     * Sets for each trucker the coordinates of the double engines that he/she wants to activate
     * @param truckerCoordinate list of arrays of coordinates
     * @param numTruckers number of truckers
     */
    public CardState setCoordinates(Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws InvalidCoordinates, GeneralError {
        if (this.state != CardState.ATTESA_RISPOSTE) {
            throw new GeneralError("Non è possibile inserire le informazioni nello stato " + this.state);
        }
        if(!trucker.getSpaceShip().checkDoubleEngine(truckerCoordinate)){
            throw new InvalidCoordinates("Not all components are DoubleEngine");
        }
        this.truckersCoordinates.addLast(truckerCoordinate);
        if (truckersCoordinates != null && truckersCoordinates.size() == numTruckers) {
            for (ArrayList<int[]> list : truckersCoordinates) {
                // la lista deve essere o piena o vuota ma non può essere null
                if (list == null) {
                    throw new GeneralError("A trucker's list in openspace after all answers is null");
                }
            }
            this.state = CardState.ESECUZIONE;
        }
        return state;
    }

    /**
     * Applies the changes provided by the card
     * @param game game in which the card has been drawn
     */
    @Override
    public CardState cardEffect(Game game) throws GeneralError {
        if (this.state != CardState.ESECUZIONE) {
            throw new GeneralError("Non è possibile eseguire card effect nello stato " + this.state);
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
        state = CardState.EXIT;
        return state;
    }
}

