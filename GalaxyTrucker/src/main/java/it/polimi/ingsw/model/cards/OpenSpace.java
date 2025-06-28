package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.EndGameEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
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

    /**
     * current state of the card, helper for debug and checking consistency
     */
    private CardState state;

    /**
     * index of the current player to handle the fact that clients have to respect a specific turn
     */
    private int indexOfCurrentTrucker;

    /**
     * Open space constructor, initialise the attributes of this class
     *
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public OpenSpace(boolean trialFlight, int level, String frontImage, String backImage, int id) {
        super("Open Space", "Empty vastness of space", trialFlight, level, frontImage, backImage, id);
        this.indexOfCurrentTrucker = 0;
        state = CardState.ACTIVATE_ENGINES;
    }

    public CardState getCardState() {
        return state;
    }

    /**
     * @return an event containing all the card details
     */
    @Override
    public synchronized ServerEvent startCard() {
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
                ParserState.ACTIVATE_ENGINES,
                null,
                Game.getIstance().getTrack(),
                this
        );
    }

    /**
     * Sets for each trucker the coordinates of the double engines that he/she wants to activate
     *
     * @param activate: decision taken by the current player
     * @param coord: coordinates of the doubleEngines to activate
     * @param batteryCoord: coordinates of the BatteryStorages from which remove the batteries
     * @return serverEvent to send to clients
     * @throws InvalidCoordinates: coordinates are not of shields or batteryStorages
     * @throws NotEnoughBatteries: not enoughBatteries indicated
     */
    @Override
    public synchronized ServerEvent activateEngines(Boolean activate, ArrayList<int[]> coord, ArrayList<int[]> batteryCoord) throws InvalidCoordinates, GeneralError, NotEnoughBatteries {
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
        return new BooleanDecision(true, ParserState.ACTIVATE_ENGINES, Game.getIstance().getTruckerList().get(indexOfCurrentTrucker).getNickname(), Game.getIstance().getTruckerList());
    }


    /**
     * Applies the changes provided by the card:
     * moves forward every player based on the engine power of each ship
     *
     * @return serverEvent to send to clients
     */
    @Override
    public synchronized ServerEvent cardEffect() throws GeneralError {
        if (this.state != CardState.ESECUZIONE) {
            throw new GeneralError("Non è possibile eseguire card effect nello stato " + this.state);
        }
        
        List<Trucker> truckers = new ArrayList<>(Game.getIstance().getTruckerList());
        int[] enginePowerPerTrucker = new int[truckers.size()];
        
        for (int i = 0; i < truckers.size(); i++) {
            Trucker trucker = truckers.get(i);
            enginePowerPerTrucker[i] = trucker.getSpaceShip().getTotalEnginePower();
        }

        for (int i = 0; i < truckers.size(); i++) {
            if (enginePowerPerTrucker[i] == 0) {
                truckers.get(i).getSpaceShip().removeAllHumans();
            }
        }
        
        for (int i = 0; i < truckers.size(); i++) {
            Trucker trucker = truckers.get(i);
            if (enginePowerPerTrucker[i] > 0) {
                Game.getIstance().moveForward(trucker, enginePowerPerTrucker[i]);
            }
            trucker.getSpaceShip().deactivateDoubleEngines();
        }

        ArrayList<Trucker> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();

        GamePersistence.saveGame(GameController.getIstance());
        if (Game.getIstance().getTruckerList().isEmpty()){
            int[] scores;
            Map<String, Integer> playersScores = new HashMap<>();
            scores = Game.getIstance().computeScores();
            for(int i=0; i<Game.getIstance().getNumPlayers(); i++){
                playersScores.put(Game.getIstance().getTruckerOut().get(i).getNickname(), scores[i]);
            }
            GamePersistence.deleteSave();
            return new EndGameEvent(playersScores);
        }
        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, Game.getIstance().getTrack());
    }

    public void setState(CardState state) {
        this.state = state;
    }

    public int getIndexOfCurrentTrucker(){
        return indexOfCurrentTrucker;
    }
}