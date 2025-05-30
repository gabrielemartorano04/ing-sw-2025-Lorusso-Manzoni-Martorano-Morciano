package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class RemoveBatteries extends CardEvent{
    private final int batteriesToRemove;

    public RemoveBatteries(boolean forAll, String nickname, int batteriesToRemove) {
        super(ServerEventType.REMOVE_BATTERIES, forAll, ParserState.REMOVE_BATTERIES, nickname);
        this.batteriesToRemove = batteriesToRemove;
    }

    public int getBatteriesToRemove() {
        return batteriesToRemove;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers(){
        return getNickname()+" is deciding where to remove batteries";
    }
}
