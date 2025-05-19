package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class BooleanDecision extends CardEvent{
    private final String message;

    public BooleanDecision(ServerEventType eventType, boolean forAll, CardState cardState, String message, String nickname) {
        super(eventType, forAll, cardState, nickname);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getInfoForOthers(){
        switch(getEventType()){
            case ROLL_DICE -> {
                return getNickname() + "got the dice result";
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
