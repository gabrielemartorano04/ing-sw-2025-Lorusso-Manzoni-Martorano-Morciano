package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class PlotGood extends CardEvent{

    public PlotGood(ServerEventType eventType, boolean forAll, CardState cardState, String nickname) {
        super(eventType, forAll, cardState, nickname);
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        //TODO: fare il visitor lato client
    }
}
