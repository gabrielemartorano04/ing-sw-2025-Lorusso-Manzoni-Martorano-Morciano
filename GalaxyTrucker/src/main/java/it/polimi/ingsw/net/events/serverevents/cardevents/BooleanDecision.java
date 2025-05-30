package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class BooleanDecision extends CardEvent{
    private final String message;

    public BooleanDecision(ServerEventType eventType, boolean forAll, ParserState parserState, String message, String nickname) {
        super(eventType, forAll, parserState, nickname);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfoForOthers(){
        switch(getEventType()){
            case ROLL_DICE -> {
                return getNickname() + "is rolling the dice";
            }
            case ACTIVATE_CANNONS -> {
                return getNickname() + "is deciding wich cannons to activate";
            }
            case ACTIVATE_ENGINES -> {
                return getNickname() + "is deciding wich engines to activate";
            }
            case ACTIVATE_SHIELD -> {
                return getNickname() + "is deciding wich shield to activate";
            }
        }
        return "";
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
