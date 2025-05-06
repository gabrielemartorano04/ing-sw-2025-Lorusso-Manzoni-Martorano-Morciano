package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Pirates;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.CoordinatesResult;
import it.polimi.ingsw.utilities.results.IntResult;
import it.polimi.ingsw.utilities.results.Result;

import java.util.ArrayList;

public class PiratesCaller implements CardCaller{
    public CardStateResult call(Pirates card, Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer) throws IllegalStateException{
        return new CardStateResult(card.addDoubleCannonsCoord(trucker, doubleCannonsCoord, numPlayer));
    }
    public CardStateResult call(Pirates card, boolean choice) throws IllegalStateException{
        return new CardStateResult(card.setChooseToGain(choice));
    }
    public CoordinatesResult call(Pirates card){
        int[] result = card.getCurrentShoot();
        return new CoordinatesResult(card.getCardState(), result);
    }

    public IntResult callRollDice(Pirates card, Game game){
        int result = card.rollDice(game);
        return new IntResult(card.getCardState(), result);
    }

    public CardStateResult callCardEffect(Pirates card, Game game){
        return new CardStateResult(card.cardEffect(game));
    }

    public CardStateResult call(Pirates card, boolean answers, Trucker turcker){
        return new CardStateResult(card.setAnswers(answers, turcker));
    }

}
