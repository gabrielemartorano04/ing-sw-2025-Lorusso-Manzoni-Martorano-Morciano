package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.StarDust;
import it.polimi.ingsw.utilities.params.Params;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.Result;

public class StarDustVisitor {
    public Result visit(StarDust card, Game game){
        return new CardStateResult(card.cardEffect(game));
    }
}
