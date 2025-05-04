package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.AbandonedStation;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidPositionException;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.GoodsResult;
import it.polimi.ingsw.utilities.results.Result;

public class AbandonedStationVisitor {
    public Result visit(AbandonedStation card, Trucker trucker, boolean landing, int numPlayer, Bank bank) throws IllegalStateException {
        return new CardStateResult(card.truckerLanding(trucker,landing,numPlayer,bank));
    }
    public Result visit(AbandonedStation card, boolean choice) throws IllegalStateException {
        Goods g = card.setChoiceToGain(choice);
        return new GoodsResult(card.getCardState(), g);
    }
    public Result visit(AbandonedStation card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws IllegalStateException, InvalidCoordinates {
        return new CardStateResult(card.setWinnerCoordinatesToAddGoods(winnerCoordinatesToAddGood, value, trucker, bank));
    }
    public Result visit(AbandonedStation card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws IllegalStateException, InvalidCoordinates, InvalidPositionException{
        return new CardStateResult(card.rearrange(trucker,coord1,coord2,value1,value2,contiune));
    }
    public Result visit(AbandonedStation card, Game game){
        return new CardStateResult(card.cardEffect(game));
    }
}
