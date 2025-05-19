package it.polimi.ingsw.net.socket.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class GainCosmicCredits extends CardEvent{
    private final int cosmicCredits;

    public GainCosmicCredits(boolean forAll, String nickname, int cosmicCredits) {
        super(ServerEventType.COSMIC_CREDITS, forAll, CardState.COSMIC_CREDITS, nickname);
        this.cosmicCredits = cosmicCredits;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
