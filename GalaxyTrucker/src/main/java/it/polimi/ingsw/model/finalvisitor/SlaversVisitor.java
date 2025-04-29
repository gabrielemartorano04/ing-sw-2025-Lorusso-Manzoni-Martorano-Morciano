package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.cards.Slavers;
import it.polimi.ingsw.param.Params;
import it.polimi.ingsw.param.Result;

public class SlaversVisitor {
    public void visit(Slavers card){}
    public Result visit(Slavers card, Params params){
        return null;
    }
}
