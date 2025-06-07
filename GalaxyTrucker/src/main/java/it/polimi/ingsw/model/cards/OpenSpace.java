package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * In order of position, starting from the leader, each trucker decides which double engine wants to activate, using batteries, for the calculation of the total engine power.
 * The total engine power of the trucker is the number of days of flight that his/her spaceship will gain on the dashboard
 */
public class OpenSpace extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
        System.out.println("startPos in startCard of OpenSpace of the first trucker is " + Game.getIstance().getTruckerList().getFirst().getPosition());
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Choose if you want to activate the double engines. If yes type the coordinates of the cannons then 'battery' and battery storage(s) coordinates . Type 'y, <x1> <y1>, <x2> <y2>,..., battery, <xs1> <ys1> - <numofbatteries>,...' or 'n': ",
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
                ParserState.ACTIVATE_ENGINES
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

        if (indexOfCurrentTrucker == Game.getIstance().getTruckerList().size()-1) {
            this.state = CardState.ESECUZIONE;
            return cardEffect();
        }
        indexOfCurrentTrucker++;
        return new BooleanDecision(ServerEventType.ACTIVATE_ENGINES, true, ParserState.ACTIVATE_ENGINES, "Choose the position of the double engines to activate", Game.getIstance().getTruckerList().get(indexOfCurrentTrucker).getNickname());
    }

    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if (this.state != CardState.ESECUZIONE) {
            throw new GeneralError("Non è possibile eseguire card effect nello stato " + this.state);
        }
        
        List<Trucker> truckers = new ArrayList<>(Game.getIstance().getTruckerList());
        int[] enginePowerPerTrucker = new int[truckers.size()];
        
        for (int i = 0; i < truckers.size(); i++) {
            Trucker trucker = truckers.get(i);
            enginePowerPerTrucker[i] = trucker.getSpaceShip().getTotalEnginePower();
        }

        //elimino tutti gli umani così in automatico withdraw elimina trucker
        for (int i = 0; i < truckers.size(); i++) {
            if (enginePowerPerTrucker[i] == 0) {
                truckers.get(i).getSpaceShip().removeAllHumans();
            }
        }
        
        for (int i = 0; i < truckers.size(); i++) {
            Trucker trucker = truckers.get(i);
            if (enginePowerPerTrucker[i] > 0) {
                System.out.println("startPos before calling moveForward of " + trucker.getNickname() + " is " + trucker.getPosition());
                Game.getIstance().moveForward(trucker, enginePowerPerTrucker[i]);
            }
            trucker.getSpaceShip().deactivateDoubleEngines();
        }

        ArrayList<String> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();
        System.out.println("sono morti (open space):" + truckerRemoved);
        Map<String, Integer> track = new HashMap<>();
        for (Trucker t : Game.getIstance().getTruckerList()) {
            track.put(t.getNickname(), t.getPosition());
        }
        GamePersistence.saveGame(Game.getIstance());
        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, track);
    }

}