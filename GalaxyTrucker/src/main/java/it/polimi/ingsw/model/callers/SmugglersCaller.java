package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Smugglers;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.*;
import it.polimi.ingsw.utilities.results.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class SmugglersCaller implements CardCaller, Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult call(Smugglers card, Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Bank bank) throws GeneralError, InvalidComponentsException{
        return new CardStateResult(card.addDoubleCannonsCoord(trucker, doubleCannonsCoord, numPlayer, bank));
    }

    public GoodsResult callPlotGood(Smugglers card) throws GeneralError{
        Goods g = card.plotCurrGood();
        return new GoodsResult(card.getCardState(), g);
    }

    public IntResult callGetBatteries(Smugglers card) throws GeneralError{
        int result = card.getNumOfBatteriesToRemove();
        return new IntResult(card.getCardState(), result);
    }

    public CardStateResult call(Smugglers card, boolean choice) throws GeneralError{
        return new CardStateResult(card.setChoiceToGain(choice));
    }

    public CardStateResult call(Smugglers card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws GeneralError, InvalidCoordinates {
        return new CardStateResult(card.setWinnerCoordinatesToAddGoods(winnerCoordinatesToAddGood, value, trucker, bank));
    }

    public CardStateResult call(Smugglers card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException{
        return new CardStateResult(card.rearrange(trucker, coord1, coord2, value1, value2, contiune));
    }

    public CardStateResult call(Smugglers card, Trucker trucker, Bank bank)throws GeneralError{
        return new CardStateResult(card.manageRemoveGoods(trucker, bank));
    }

    public GoodsListResult call(Smugglers card, Trucker trucker)throws GeneralError{
        ArrayList<Goods> result = card.plotGoodsList(trucker);
        return new GoodsListResult(card.getCardState(), result);
    }

    public CardStateResult call(Smugglers card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws GeneralError, InvalidGoodChosen, NotEnoughGoodsChosen{
        return new CardStateResult(card.removeAsking(indexes, trucker, bank));
    }

    public CardStateResult call(Smugglers card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws GeneralError, InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries{
        return new CardStateResult(card.removeBatteries(coordinatesToRemoveBatteries, trucker));
    }

    public CardStateResult call(Smugglers card, Game game) throws GeneralError{
        return new CardStateResult(card.cardEffect(game));
    }

}
