package it.polimi.ingsw.net.events.serverevents.cardevents;


import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class HumanRemovedConfirmed extends CardEvent {

    private final SpaceShip ship;
    private final String message;


    public HumanRemovedConfirmed(ServerEventType eventType, String nickname, SpaceShip ship, String message) {
        super(eventType, true, ParserState.HUMAN_REMOVED, nickname);
        this.ship = ship;
        this.message = message;
    }

    public SpaceShip getShip() {
        return ship;
    }
    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {

    }
}
