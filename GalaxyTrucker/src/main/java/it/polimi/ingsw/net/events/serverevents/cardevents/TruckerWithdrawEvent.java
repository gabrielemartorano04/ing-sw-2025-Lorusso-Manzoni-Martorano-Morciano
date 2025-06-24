package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

public class TruckerWithdrawEvent extends CardEvent {
    private String message, nickname;
    private boolean choice;

    public TruckerWithdrawEvent(String nickname,boolean choice) {
        super(true, ParserState.DO_NOTHING, nickname);
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
