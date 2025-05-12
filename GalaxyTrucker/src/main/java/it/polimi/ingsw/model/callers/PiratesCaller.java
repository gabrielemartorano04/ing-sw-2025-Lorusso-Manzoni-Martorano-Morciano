package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Pirates;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.CoordinatesResult;
import it.polimi.ingsw.utilities.results.IntResult;

import java.io.Serializable;
import java.util.ArrayList;

public class PiratesCaller implements CardCaller, Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult call(Pirates card, Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer) throws GeneralError, InvalidComponentsException {
        return new CardStateResult(card.addDoubleCannonsCoord(trucker, doubleCannonsCoord, numPlayer));
    }
    public CardStateResult call(Pirates card, boolean choice) throws GeneralError {
        return new CardStateResult(card.setChooseToGain(choice));
    }
    public CoordinatesResult call(Pirates card) throws GeneralError {
        int[] result = card.getCurrentShoot();
        return new CoordinatesResult(card.getCardState(), result);
    }

    public IntResult callRollDice(Pirates card, Game game) throws GeneralError {
        int result = card.rollDice(game);
        return new IntResult(card.getCardState(), result);
    }

    public CardStateResult callCardEffect(Pirates card, Game game) throws GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }

    public CardStateResult call(Pirates card, boolean answers, Trucker turcker) throws GeneralError {
        return new CardStateResult(card.setAnswers(answers, turcker));
    }

}
