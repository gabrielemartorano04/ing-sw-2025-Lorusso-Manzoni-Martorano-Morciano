package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents the result of rolling a dice during a player's turn in the game.
 */
public class RollDiceResult extends CardEvent {

    /**
     * Index of the current meteor of shoot
     */
    private final int currIndex;

    /**
     * Result of the roll
     */
    private final int diceResult;

    /**
     * Specify the dimension and the direction of the shoot
     */
    private final int[] shoot;

    /**
     * Specify the dimension and the direction of the meteor
     */
    private final int[] meteor;

    /**
     * Event constructor
     * @param parserState state of the parser
     * @param currIndex index of the shoot or meteor
     * @param shoot specify the dimension and the direction of the shoot
     * @param meteor specify the dimension and the direction of the meteor
     * @param diceResult result of the roll
     * @param nickname of the player
     * @param forAll specify if the message is for a player or everyone
     * @param truckers list of players
     */
    public RollDiceResult(ParserState parserState, int currIndex, int[] shoot, int [] meteor, int diceResult, String nickname, boolean forAll, ArrayList<Trucker> truckers){
        super(forAll, parserState, nickname, truckers);
        this.currIndex = currIndex;
        this.shoot=shoot;
        this.meteor=meteor;
        this.diceResult = diceResult;
    }

    /**
     * @return the current index of shoot or meteor
     */
    public int getCurrIndex() {return currIndex;}

    /**
     * @return the specific dimension and direction of the shoot
     */
    public int[] getShoot(){
        return this.shoot;
    }

    /**
     * @return the specific dimension and direction of the meteor
     */
    public int[] getMeteor(){
        return meteor;
    }

    /**
     * @return the result of the roll
     */
    public int getDiceResult() {return diceResult;}

    /**
     * @return a string message describing the player's current activity
     */
    public String getInfoForOthers(){
        return getNickname()+" got "+diceResult+ " from the dice\n";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}



