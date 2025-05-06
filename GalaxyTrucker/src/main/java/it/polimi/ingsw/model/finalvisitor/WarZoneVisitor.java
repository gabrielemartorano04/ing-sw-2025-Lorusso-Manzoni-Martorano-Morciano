package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.WarZone;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.*;
import it.polimi.ingsw.utilities.params.Params;
import it.polimi.ingsw.utilities.results.*;

import java.util.ArrayList;

public class WarZoneVisitor {
    public Result visit(WarZone card, Game game) {
        if(card.getCardState()== CardState.WARZONE){
            return new CardStateResult(card.combatZone(game));
        }else if(card.getCardState() == CardState.ROLL_DICE){
            card.rollDice(game);
            return new CardStateResult(card.getCardState());
        }else{
            return new CardStateResult(card.cardEffect(game));
        }
    }

    public Result visit(WarZone card, Game game, Trucker trucker, ArrayList<int[]> coord) throws InvalidComponentsException{
        if(card.getCardState()== CardState.WAIT_CANNONS){
            card.setCannonCoordinates(game,trucker, coord);
        }else {
            card.setEnginesCoordinates(game,trucker, coord);
        }
        return new CardStateResult(card.getCardState());
    }

    public Result visit(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCoordinates){
        card.findWeakestTrucker(game, truckersCoordinates);
        return new CardStateResult(card.getCardState());
    }

    public Result visit(WarZone card, Trucker trucker, Bank bank){
        return new CardStateResult(card.manageRemoveGoods(trucker, bank));
    }

    public Result visit(WarZone card, Trucker trucker){
        ArrayList<Goods> goodsList = card.plotGoodsList(trucker);
        return new GoodsListResult(card.getCardState(), goodsList);
    }

    public Result visit(WarZone card, ArrayList<Integer> indexes, Trucker trucker, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen{
        card.removeAsking(indexes, trucker, bank);
        return new CardStateResult(card.getCardState());
    }

    public Result visit(WarZone card) {
        if (card.getCardState() == CardState.PLOT_BATTERIES) {
            int result = card.getNumOfBatteriesToRemove();
            return new IntResult(card.getCardState(), result);
        }else {
            int[] result = card.getCurrentMeteor();
            return new CoordinatesResult(card.getCardState(), result);
        }
    }

    public Result visit(WarZone card, ArrayList<int[]> coordinatesToRemoveBatteries, Trucker trucker) throws InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries{
        card.removeBatteries(coordinatesToRemoveBatteries, trucker);
        return new CardStateResult(card.getCardState());
    }

    public Result visit(WarZone card, boolean choice){
        card.setAnswers(choice);
        return new CardStateResult(card.getCardState());
    }

    public Result visit(WarZone card, ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates{
        return new CardStateResult(card.removeCrew(coordinates));
    }

}
