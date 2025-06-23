package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GamePersistence;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.events.serverevents.cardevents.BooleanDecision;
import it.polimi.ingsw.net.events.serverevents.cardevents.CardFinished;
import it.polimi.ingsw.net.events.serverevents.cardevents.HumanCoordinates;
import it.polimi.ingsw.net.events.serverevents.cardevents.StartCardEvent;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.view.ParserState;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that manages the single card abandoned ship
 * In order of position, starting from the leader, the trucker decides, checking the crew members requirement, if he/she wants to lose members of the crew and days of flight in exchange for cosmic credits.
 * Only one trucker car take the station and is possible that nobody wants to take it
 */

public class AbandonedShip extends Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * number of flight days to lose
     */
    private final int days;

    /**
     * number of cosmic credits you win if you decide to land
     */
    private final int credits;

    /**
     * number of aliens or humans you have to lose if you decide to land
     */
    private final int numHuman;

    /**
     * player that has decided to land if exists a client that has decided to land
     */
    private Trucker truckerThatHasDecided;

    /**
     * coordinates of all the cabins the player has decided to remove humans or aliens from
     */
    private ArrayList<int[]> coordinates = null;

    /**
     * current state of the card, helper for debug and checking consistency
     */
    private CardState state;

    /**
     * index of the current player to handle the fact that clients have to respect a specific turn
     */
    private int indexOfCurrTrucker;


    /**
     * Abandoned ship constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param numHuman number of crew members to lose
     * @param credits number of cosmic credits gains
     * @param days number of days of flight to lose
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public AbandonedShip(boolean trialFlight, int level, int numHuman, int credits, int days, String frontImage, String backImage, int id) {
        super("Abandoned Ship", "A drifting ghost ship",  trialFlight, level, frontImage, backImage, id);
        this.numHuman = numHuman;
        this.credits = credits;
        this.days = days;
        this.truckerThatHasDecided = null;
        this.indexOfCurrTrucker = 0;
        this.state = CardState.LAND;
    }

    /**
     * @return an event containing all the card details
     */
    @Override
    public ServerEvent startCard() {
        return new StartCardEvent(
                this.getName(),
                this.getDescription(),
                Game.getIstance().getTruckerList().getFirst().getNickname(),
                "Do you want to land on this Abandoned Ship?",
                days,
                credits,
                numHuman,
                null,
                null,
                -1,
                -1,
                -1,
                null,
                null,
                null,
                null,
                ParserState.LAND,
                this
        );
    }


    /**
     * sets truckerThatHasDecided based on the answer by the current client and updates the next client if landing is false
     * @param landing: the choice taken by the current client
     * @return serverEvent to send to all clients
     */
    @Override
    public ServerEvent truckerLanding(boolean landing) throws GeneralError {
        if(state != CardState.LAND) {
            throw new GeneralError("SetTruckerThatHasDecided not callable in state: " + state);
        }
        if(landing) {
            truckerThatHasDecided = Game.getIstance().getTrucker(indexOfCurrTrucker);
            state = CardState.HUMANSTOREMOVE;
            return new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, true, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname(),Game.getIstance().getTrucker(indexOfCurrTrucker).getSpaceShip(),null,"");
        }else{
            if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()-1){
                state = CardState.ESECUZIONE;
                return cardEffect();
            }else{
                indexOfCurrTrucker++;
                return new BooleanDecision(ServerEventType.LAND, true, ParserState.LAND, "Do you want to land on this Abandoned Ship?",  Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
            }
        }
    }

    /**
     * Sets the coordinates of the cabins from which the player that decided to land wants to
     * remove the crew members required, after checking if they are consistent
     * @param coordinates list of arrays of coordinates of the cabins
     * @return serverEvent to send to all clients
     */
    @Override
    public ServerEvent setHumansCoordinates(ArrayList<int[]> coordinates) throws InvalidCoordinates, GeneralError {
        if(state != CardState.HUMANSTOREMOVE) {
            throw new GeneralError("Can't call setCoordinates in the state: " + state);
        }
        if(truckerThatHasDecided ==null){
            throw new GeneralError("Tried to set coordinates when truckerThatHasDecided is null");
        }
        try{
            truckerThatHasDecided.getSpaceShip().checkCabin(coordinates, numHuman);
            this.coordinates = coordinates;
            state = CardState.ESECUZIONE;
            return cardEffect();
        }catch (InvalidCoordinates e){
            throw new InvalidCoordinates(e.getMessage());
        }
    }

    /**
     * Applies the changes provided by the card:
     * adds cosmic credits to the player landed and make him/her lose crew members and flight days
     *
     * @return serverEvent to send to clients
     */
    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if(state != CardState.ESECUZIONE) {
            throw new GeneralError("Non puoi settare fare CardEffect nello stato: " + state);
        }
        if (truckerThatHasDecided != null) {
            truckerThatHasDecided.getSpaceShip().removeNumHuman(coordinates,numHuman);
            Game.getIstance().moveBackward(truckerThatHasDecided,days);
            truckerThatHasDecided.addCosmicCredit(credits);
        }
        ArrayList<Trucker> truckerRemoved;
        truckerRemoved = Game.getIstance().endTurn();

        GamePersistence.saveGame(GameController.getIstance());

        return new CardFinished(true, Game.getIstance().getTruckerList(), truckerRemoved, Game.getIstance().getTrack());
    }


    public int getDays() {
        return days;
    }

    public int getCredits() {
        return credits;
    }

    public int getNumHuman() {
        return numHuman;
    }

    public int getIndexOfCurrTrucker() {
        return indexOfCurrTrucker;
    }
}
