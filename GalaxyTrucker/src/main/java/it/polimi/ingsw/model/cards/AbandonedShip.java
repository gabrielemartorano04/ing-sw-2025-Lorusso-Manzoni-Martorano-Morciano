package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.view.ParserState;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that manages the single card abandoned ship
 * In order of position, starting from the leader, the trucker decides, checking the crew members requirement, if he/she wants to lose members of the crew and days of flight in exchange for cosmic credits.
 * Only one trucker car take the station and is possible that nobody wants to take it
 */
public class AbandonedShip extends Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int days;
    private final int credits;
    private final int numHuman;
    private Trucker truckerThatHasDecided = null; //è il trucker che ha deciso di atterrare, è null se nessuno ha deciso di atterrare
    private ArrayList<int[]> coordinates = null; //è la lista di coordinate da cui rimuovere gli umani che è settata dal controller
    private CardState state;
    private int indexOfCurrTrucker;


    /**
     * Abandoned ship constructor, initialise the attributes of this class
     * @param trialFlight specify if truckers are playing trial flight
     * @param level level of the game
     * @param numHuman number of crew members to lose
     * @param credits number of cosmic credits gain
     * @param days number of days of flight to lose
     * @param frontImage front image of the card
     * @param backImage back image of the card
     */
    public AbandonedShip(boolean trialFlight, int level, int numHuman, int credits, int days, String frontImage, String backImage) {
        super("Abandoned Ship", "A drifting ghost ship",  trialFlight, level, frontImage, backImage);
        this.numHuman = numHuman;
        this.credits = credits;
        this.days = days;
        this.truckerThatHasDecided = null;
        this.indexOfCurrTrucker = 0;
        this.state = CardState.LAND;
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
     * Returns the number of crew members to lose
     * @return crew members to lose
     */
    public int getNumHuman() {
        return numHuman;
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
                ParserState.LAND
        );
    }


    /**
     * Sets the only trucker that has chosen to take the ship
     * @param landing: the choice taken by the current client
     */
    @Override
    public ServerEvent truckerLanding(boolean landing) throws GeneralError {
        if(state != CardState.LAND) {
            throw new GeneralError("SetTruckerThatHasDecided not callable in state: " + state);
        }
        if(landing) {
            truckerThatHasDecided = Game.getIstance().getTrucker(indexOfCurrTrucker);
            state = CardState.HUMANSTOREMOVE;
            return new HumanCoordinates(ServerEventType.HUMAN_COORDINATES, false, Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
        }else{
            if (indexOfCurrTrucker == Game.getIstance().getTruckerList().size()){
                state = CardState.ESECUZIONE;
                return cardEffect();
            }else{
                indexOfCurrTrucker++;
                return new BooleanDecision(ServerEventType.LAND, true, ParserState.LAND, "Do you want to land on this Abandoned Ship?",  Game.getIstance().getTrucker(indexOfCurrTrucker).getNickname());
            }
        }
    }

    /**
     * Sets the coordinates of the cabins from which he wants to remove the crew members required
     * @param coordinates list of arrays of coordinates of the cabins
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
     * Applies the changes provided by the card
     */

    @Override
    public ServerEvent cardEffect() throws GeneralError {
        if(state != CardState.ESECUZIONE) {
            throw new GeneralError("Non puoi settare fare CardEffect nello stato: " + state);
        }
        if (truckerThatHasDecided != null) {
            truckerThatHasDecided.getSpaceShip().removeNumHuman(coordinates);
            Game.getIstance().moveBackward(truckerThatHasDecided,days);
            truckerThatHasDecided.addCosmicCredit(credits);
        }
        ArrayList<Trucker> turckerRemoved = new ArrayList<>();
        turckerRemoved = Game.getIstance().endTurn();
        return new CardFinished(false, Game.getIstance().getTruckerList(), turckerRemoved);
    }

}
