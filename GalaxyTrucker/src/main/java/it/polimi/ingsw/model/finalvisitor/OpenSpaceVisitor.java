package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.cards.OpenSpace;
import it.polimi.ingsw.param.params.Params;
import it.polimi.ingsw.param.results.Result;

public class OpenSpaceVisitor {
    public Result visit(OpenSpace card, Params params){
        return null;
        //return new GenericResult<>(CardState.ESECUZIONE);
    }
}
