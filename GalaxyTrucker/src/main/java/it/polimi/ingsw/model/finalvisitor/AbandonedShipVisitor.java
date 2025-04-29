package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.param.Params;
import it.polimi.ingsw.param.Result;

public class AbandonedShipVisitor {
    public void visit(AbandonedShip card) {
        //TODO: implementare con switch case su CardState la chiamata al metodo corretto di AbandonedShip
    }
    public Result visit(AbandonedShip card, Params params){
        //TODO: chiamate ai metodi parametrici in base allo state
        return null;
    }
}
