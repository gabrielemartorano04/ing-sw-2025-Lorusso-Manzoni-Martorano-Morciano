package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class RemoveBatteries extends CardEvent{
    private final int batteriesToRemove;

    public RemoveBatteries(boolean forAll, CardState cardState, String nickname, int batteriesToRemove) {
        super(ServerEventType.REMOVE_BATTERIES, forAll, cardState, nickname);
        this.batteriesToRemove = batteriesToRemove;
    }

    public int getBatteriesToRemove() {
        return batteriesToRemove;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {

    }
}
