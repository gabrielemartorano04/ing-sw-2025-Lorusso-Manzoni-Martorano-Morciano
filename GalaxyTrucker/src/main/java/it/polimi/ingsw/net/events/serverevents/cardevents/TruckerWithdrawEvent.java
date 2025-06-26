package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

/**
 * Represents an event triggered when a trucker decides to withdraw from the game
 * or indicates their willingness to continue playing
 */
public class TruckerWithdrawEvent extends CardEvent {

    /**
     * Nickname of the player
     */
    private final String nickname;

    /**
     * Choise taken by the player to withdraw or continue playng
     */
    private final boolean choice;

    /**
     * Event constructor
     * @param nickname of the player
     * @param choice taken by the player
     */
    public TruckerWithdrawEvent(String nickname,boolean choice) {
        super(true, ParserState.DO_NOTHING, nickname,new ArrayList<>());
        this.nickname = nickname;
        this.choice = choice;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the choice taken by the player
     */
    public boolean getChoice(){
        return choice;
    }

    /**
     * @return  a string message describing the player's current activity
     */
    @Override
    public String getMessage() {
        if(choice){
            return nickname + " has withdrawn from the game";
        }
        else{
            return nickname + " wants to play";
        }
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
