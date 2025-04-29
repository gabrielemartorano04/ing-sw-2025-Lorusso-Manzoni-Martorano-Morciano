package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.cards.StarDust;
import it.polimi.ingsw.param.Params;
import it.polimi.ingsw.param.Result;

public class StarDustVisitor {
    public void visit(StarDust card) {}
    public Result visit(StarDust card, Params params){
        return null;
    }
}
