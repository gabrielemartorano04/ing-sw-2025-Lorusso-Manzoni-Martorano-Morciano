package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class PlotGood extends CardEvent{
    private final Goods good;

    public PlotGood(boolean forAll, String nickname, Goods good) {
        super(ServerEventType.PLOT_GOODS, forAll, ParserState.PLOT_GOOD, nickname);
        this.good = good;
    }

    public Goods getGood() {
        return good;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers(){
        return getNickname()+" is deciding which goods to gain\n";
    }
}
