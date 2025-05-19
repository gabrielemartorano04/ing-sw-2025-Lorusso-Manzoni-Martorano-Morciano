package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class PlotGood extends CardEvent{
    private final Goods good;

    public PlotGood(boolean forAll, CardState cardState, String nickname, Goods good) {
        super(ServerEventType.PLOT_GOODS, forAll, cardState, nickname);
        this.good = good;
    }

    public Goods getGood() {
        return good;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        //TODO: fare il visitor lato client
    }

    public String getInfoForOthers(){
        return getNickname()+" is deciding which goods to gain\n";
    }
}
