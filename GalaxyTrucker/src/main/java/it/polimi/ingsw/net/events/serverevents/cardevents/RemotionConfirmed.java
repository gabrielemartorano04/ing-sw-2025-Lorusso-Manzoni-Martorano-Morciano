package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class RemotionConfirmed extends CardEvent{
    private final String confirm;
    private SpaceShip spaceShip;

    public RemotionConfirmed(ServerEventType type, String nickname,SpaceShip spaceShip, String confirm) {
        super(type, true, ParserState.REMOTION_CONFIRMED, nickname);
        this.confirm = confirm;
        this.spaceShip=spaceShip;
    }

    public SpaceShip getShip(){
        return spaceShip;
    }

    public String getInfoForOthers(){
        return getNickname()+" received a remotion confirm";
    }

    public String getConfirm() {
        return confirm;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
