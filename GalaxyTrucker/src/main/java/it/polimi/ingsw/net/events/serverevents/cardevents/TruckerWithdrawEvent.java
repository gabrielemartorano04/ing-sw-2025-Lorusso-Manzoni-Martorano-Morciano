package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class TruckerWithdrawEvent extends CardEvent {
    private String message;
    private final String nickname;
    private final boolean choice;

    public TruckerWithdrawEvent(String nickname,boolean choice) {
        super(true, ParserState.DO_NOTHING, nickname,new ArrayList<>());
        message = "";
        this.nickname = nickname;
        this.choice = choice;
    }

    @Override
    public String getMessage() {
        if(choice){
            message+= nickname + " has withdrawn from the game";
        }
        else{
            message+= nickname + " wants to play";
        }
        return message;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean getChoice(){
        return choice;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
