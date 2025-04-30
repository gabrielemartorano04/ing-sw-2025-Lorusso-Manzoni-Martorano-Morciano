package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.param.params.Params;
import it.polimi.ingsw.param.results.CardStateResult;
import it.polimi.ingsw.param.results.Result;

public class AbandonedShipVisitor {
    public Result visit(AbandonedShip card, Trucker trucker, boolean landing, int numplayer) {
        if (CardState.ATTESA_RISPOSTE == card.getCardState()) {
            return new CardStateResult(card.setTruckerThatHasDecided(trucker, landing, numplayer));
        }
        return null;
    }
}



