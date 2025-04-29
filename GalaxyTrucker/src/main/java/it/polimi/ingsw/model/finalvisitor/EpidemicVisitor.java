package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.Epidemic;
import it.polimi.ingsw.param.Params;
import it.polimi.ingsw.param.Result;

public class EpidemicVisitor {
    public void visit(AbandonedShip abandonedShip) {
            //TODO: implementare con switch case su CardState la chiamata al metodo corretto di Epidemic
    }
    public Result visit(Epidemic card, Params params){
        return null;
    }
}
