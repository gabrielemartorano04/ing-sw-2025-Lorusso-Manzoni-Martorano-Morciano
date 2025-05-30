package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;

public class ShowOtherShipResponse extends ServerEvent {
    private Component[][] spaceship;
    private String nickname;

    public ShowOtherShipResponse(Component[][] spaceship, String nickname) {
        super(ServerEventType.SHOW_OTHER_SHIP,false);
        this.spaceship = spaceship;
        this.nickname = nickname;
    }
    public Component[][] getSpaceship() {
        return spaceship;
    }
    public String getNickname() {
        return nickname;
    }
    @Override
    public String getMessage() {
        return "Show "+nickname+"'s ship";
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
