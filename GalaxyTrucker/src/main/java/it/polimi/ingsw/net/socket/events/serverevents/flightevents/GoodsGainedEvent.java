package it.polimi.ingsw.net.socket.events.serverevents.flightevents;

import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class GoodsGainedEvent extends ServerEvent {
    private String message;
    Goods good;

    public GoodsGainedEvent(Goods good) {
        super(ServerEventType.GOOD_GAINED, false);
        message = "Goods gained";
        this.good = good;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Goods getGoodGained() {
        return good;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
