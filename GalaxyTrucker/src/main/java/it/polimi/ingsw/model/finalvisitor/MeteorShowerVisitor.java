package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.MeteorShower;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.CoordinatesResult;
import it.polimi.ingsw.utilities.results.Result;

public class MeteorShowerVisitor {
    public Result visit(MeteorShower card){
        int[] result = card.getCurrentMeteor();
        return new CoordinatesResult(card.getCardState(), result);
    }
    public Result visit(MeteorShower card, Game game){
        Result result = null;
        if(card.getCardState() == CardState.ROLL_DICE){
            result =  new CardStateResult(card.rollDice(game));
        } else if (card.getCardState() == CardState.ESECUZIONE){
            result = new CardStateResult(card.cardEffect(game));
        }
        return result;
    }
    public Result visit(MeteorShower card, boolean answer){
        return new CardStateResult(card.setAnswers(answer));
    }
}
