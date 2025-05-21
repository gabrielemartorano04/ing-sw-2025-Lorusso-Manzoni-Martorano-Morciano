package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class RemotionConfirmed extends CardEvent{
    private final String confirm;

    public RemotionConfirmed( String nickname, String confirm) {
        super(ServerEventType.GOODS_REMOVED, false, ParserState.REMOTION_CONFIRMED, nickname);
        this.confirm = confirm;
    }

    public String getConfirm() {
        return confirm;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
