package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.OpenSpace;
import it.polimi.ingsw.param.GenericResult;
import it.polimi.ingsw.param.Params;
import it.polimi.ingsw.param.Result;

public class OpenSpaceVisitor {
    public void visit(OpenSpace card) {}
    public Result visit(OpenSpace card, Params params){
        return null;
        //return new GenericResult<>(CardState.ESECUZIONE);
    }
}
