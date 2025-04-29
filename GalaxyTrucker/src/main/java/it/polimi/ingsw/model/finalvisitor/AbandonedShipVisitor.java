package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.param.params.Param3;
import it.polimi.ingsw.param.params.Params;
import it.polimi.ingsw.param.results.CardStateResult;
import it.polimi.ingsw.param.results.Result;

public class AbandonedShipVisitor {
    public Result visit(AbandonedShip card, Param3<Trucker, Boolean, Integer> params){
        //TODO: chiamate ai metodi parametrici in base allo state
        switch(card.getCardState()){
            case CardState.ATTESA_RISPOSTE -> {
                return new CardStateResult(card.setTruckerThatHasDecided(params.getFirst(), params.getSecond(), params.getThird()));
            }
        }
        return null;
    }
}
