package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class GainCosmicCredits extends CardEvent{
    private final int cosmicCredits;
    private final int daysLost;

    public GainCosmicCredits(boolean forAll, String nickname, int cosmicCredits, int daysLost) {
        super(ServerEventType.COSMIC_CREDITS, forAll, ParserState.COSMIC_CREDITS, nickname);
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
}
