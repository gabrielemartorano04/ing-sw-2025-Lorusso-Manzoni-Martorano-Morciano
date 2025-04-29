package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.cards.WarZone;
import it.polimi.ingsw.param.Params;
import it.polimi.ingsw.param.Result;

public class WarZoneVisitor {
    public void visit(WarZone card){}
    public Result visit(WarZone card, Params params){
        return null;
    }
}
