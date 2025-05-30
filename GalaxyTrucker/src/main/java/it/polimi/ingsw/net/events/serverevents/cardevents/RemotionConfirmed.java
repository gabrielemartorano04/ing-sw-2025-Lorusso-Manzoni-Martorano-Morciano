package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class RemotionConfirmed extends CardEvent{
    private final String confirm;

    public RemotionConfirmed(ServerEventType type, String nickname, String confirm) {
        super(type, false, ParserState.REMOTION_CONFIRMED, nickname);
        this.confirm = confirm;
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
