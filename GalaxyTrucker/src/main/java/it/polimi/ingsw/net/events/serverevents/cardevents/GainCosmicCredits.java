package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class GainCosmicCredits extends CardEvent{
    private final int cosmicCredits;
    private final int daysLost;

    public GainCosmicCredits(boolean forAll, String nickname, int cosmicCredits, int daysLost, ArrayList<Trucker> truckers) {
        super(forAll, ParserState.COSMIC_CREDITS, nickname,truckers);
        this.cosmicCredits = cosmicCredits;
        this.daysLost = daysLost;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    public int getDaysLost() {
        return daysLost;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String getInfoForOthers() {
        return getNickname()+" is deciding to gain cosmic credits or not.\n";
    }
}
