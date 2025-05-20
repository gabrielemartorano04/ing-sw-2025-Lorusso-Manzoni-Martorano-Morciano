package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * In order of position, starting from the leader, each trucker decides which double engine wants to activate, using batteries, for the calculation of the total engine power.
 * The total engine power of the trucker is the number of days of flight that his/her spaceship will gain on the dashboard
 */
public class OpenSpace extends Card implements Serializable {
    private static final long serialVersionUID = 1L;

    //lista che contiene per ogni trucker la lista di coordinates dei motori doppi che vuole attivare
    //la lista esterna associa ogni puntatore a un trucker, quindi la lunghezza della lista esterna è quella di truckers.
    //mantiene l'ordine dei truckers nel fare questa associazione
    private ArrayList<ArrayList<int[]>> truckersCoordinates = new ArrayList<>();

    private CardState state;
    private int indexOfCurrentTrucker;


    /**
     * Open space constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public OpenSpace(boolean trialFlight, int level, String frontImage, String backImage) {
        super("Open Space", "Empty vastness of space", trialFlight, level, frontImage, backImage);
        this.indexOfCurrentTrucker = 0;
        state = CardState.ACTIVATE_ENGINES;
    }

    public CardState getCardState() {
        return state;
    }

    @Override
    public ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Choose the position of the double engines to activate",
                -1,
                -1,
                -1,
                null,
                null,
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

    /**
     * Sets for each trucker the coordinates of the double engines that he/she wants to activate
     * @param coord list of arrays of coordinates
     */
    @Override
    public ServerEvent activateEngines(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws InvalidCoordinates, GeneralError, NotEnoughBatteries {
        if (this.state != CardState.ACTIVATE_ENGINES) {
            throw new GeneralError("Non è possibile inserire le informazioni nello stato " + this.state);
        }
        if(activate){
            if(!Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().checkBatteryStorage(batteryCoord)){
                throw new InvalidCoordinates("Not all coordinates are battery storage");
            }
            if(!Game.getIstance().getTruckerList().get(indexOfCurrentTrucker).getSpaceShip().checkDoubleEngine(coord)){
                if(Game.getIstance().getTruckerList().get(indexOfCurrentTrucker).getSpaceShip().getNumDoubleEngine() == 0 ){
                    indexOfCurrentTrucker++;
                    throw new InvalidCoordinates("You don't have any double engines");

                }
                throw new InvalidCoordinates("Not all components are DoubleEngine");
            }

            Game.getIstance().getTrucker(indexOfCurrentTrucker).getSpaceShip().activateDoubleEngines(coord, batteryCoord);

        }

        if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()) {
            this.state = CardState.ESECUZIONE;
            return cardEffect();
        }
        indexOfCurrentTrucker++;
        return new BooleanDecision(ServerEventType.ACTIVATE_ENGINES, true, this.getCardState(), "Choose the position of the double engines to activate", Game.getIstance().getTruckerList().get(indexOfCurrentTrucker).getNickname());
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
        int[] enginePowerPerTrucker = new int[truckers.size()];
        int i = 0;
        for (Trucker trucker : truckers) {
            enginePowerPerTrucker[i] = trucker.getSpaceShip().getTotalEnginePower();
            if(enginePowerPerTrucker[i] == 0){
                Game.getIstance().withdraw(trucker);
            }
            i++;
            trucker.getSpaceShip().deactivateDoubleEngines();
        }
        for (i = 0; i < enginePowerPerTrucker.length; i++) {
            Game.getIstance().moveForward(truckers.get(i), enginePowerPerTrucker[i]);
        }
        state = CardState.EXIT;
        Game.getIstance().endTurn();
        return new CardFinished(true, this.getCardState(), Game.getIstance().getTruckerList());
    }

}

