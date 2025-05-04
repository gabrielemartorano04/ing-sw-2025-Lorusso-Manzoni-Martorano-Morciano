package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Pirates;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.CoordinatesResult;
import it.polimi.ingsw.utilities.results.IntResult;
import it.polimi.ingsw.utilities.results.Result;

import java.util.ArrayList;

public class PiratesVisitor {
    public Result visit(Pirates card, Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer) throws IllegalStateException{
        return new CardStateResult(card.addDoubleCannonsCoord(trucker, doubleCannonsCoord, numPlayer));
    }
    public Result visit(Pirates card, boolean choice) throws IllegalStateException{
        return new CardStateResult(card.setChooseToGain(choice));
    }
    public Result visit(Pirates card){
        int[] result = card.getCurrentShoot();
        return new CoordinatesResult(card.getCardState(), result);
    }
    public Result visit(Pirates card, Game game){
        if(card.getCardState() == CardState.ROLL_DICE){
            int result = card.rollDice(game);
            return new IntResult(card.getCardState(), result);
        }else{
            return new CardStateResult(card.cardEffect(game));
        }
    }
    public Result visit(Pirates card, boolean answers, Trucker turcker){
        return new CardStateResult(card.setAnswers(answers, turcker));
    }

}
