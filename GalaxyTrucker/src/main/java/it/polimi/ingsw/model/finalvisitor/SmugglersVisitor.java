package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Smugglers;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.*;
import it.polimi.ingsw.utilities.params.Params;
import it.polimi.ingsw.utilities.results.*;

import java.util.ArrayList;

public class SmugglersVisitor {
    public Result visit(Smugglers card, Trucker trucker, ArrayList<int[]> doubleCannonsCoord, int numPlayer, Bank bank) throws IllegalStateException, InvalidComponentsException{
        return new CardStateResult(card.addDoubleCannonsCoord(trucker, doubleCannonsCoord, numPlayer, bank));
    }

    public Result visit(Smugglers card){
        if(card.getCardState() == CardState.PLOT_GOOD) {
            Goods g = card.plotCurrGood();
            return new GoodsResult(card.getCardState(), g);
        } else {
            int result = card.getNumOfBatteriesToRemove();
            return new IntResult(card.getCardState(), result);
        }
    }

    public Result visit(Smugglers card, boolean choice){
        return new CardStateResult(card.setChoiceToGain(choice));
    }

    public Result visit(Smugglers card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws IllegalStateException, InvalidCoordinates {
        return new CardStateResult(card.setWinnerCoordinatesToAddGoods(winnerCoordinatesToAddGood, value, trucker, bank));
    }

    public Result visit(Smugglers card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws IllegalStateException, InvalidCoordinates, InvalidPositionException{
        return new CardStateResult(card.rearrange(trucker, coord1, coord2, value1, value2, contiune));
    }

    public Result visit(Smugglers card, Trucker trucker, Bank bank){
        return new CardStateResult(card.manageRemoveGoods(trucker, bank));
    }

    public Result visit(Smugglers card, Trucker trucker){
        ArrayList<Goods> result = card.plotGoodsList(trucker);
        return new GoodsListResult(card.getCardState(), result);
    }

    public Result visit(Smugglers card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen{
        return new CardStateResult(card.removeAsking(indexes, trucker, bank));
    }

    public Result visit(Smugglers card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries{
        return new CardStateResult(card.removeBatteries(coordinatesToRemoveBatteries, trucker));
    }

    public Result visit(Smugglers card, Game game){
        return new CardStateResult(card.cardEffect(game));
    }

}
