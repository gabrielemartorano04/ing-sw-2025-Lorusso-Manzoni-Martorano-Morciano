package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.WarZone;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.*;
import it.polimi.ingsw.utilities.results.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class WarZoneCaller implements CardCaller , Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult call(WarZone card, Game game) throws WrongCallerException, GeneralError {
        if(card.getCardState()== CardState.WARZONE){
            return new CardStateResult(card.combatZone(game));
        }else if(card.getCardState() == CardState.ROLL_DICE){
            card.rollDice(game);
            return new CardStateResult(card.getCardState());
        }else{
            return new CardStateResult(card.cardEffect(game));
        }
    }

    public CardStateResult call(WarZone card, Game game, TruckerController truckerController, ArrayList<int[]> coord) throws InvalidComponentsException, WrongCallerException, GeneralError {
        if(card.getCardState()== CardState.WAIT_CANNONS){
            card.setCannonCoordinates(game, truckerController, coord);
        }else {
            card.setEnginesCoordinates(game, truckerController, coord);
        }
        return new CardStateResult(card.getCardState());
    }

    public CardStateResult call(WarZone card, Game game, ArrayList<ArrayList<int[]>> truckersCoordinates) throws WrongCallerException, GeneralError {
        card.findWeakestTrucker(game, truckersCoordinates);
        return new CardStateResult(card.getCardState());
    }

    public CardStateResult call(WarZone card, TruckerController truckerController, Bank bank) throws WrongCallerException, GeneralError {
        return new CardStateResult(card.manageRemoveGoods(truckerController, bank));
    }

    public GoodsListResult call(WarZone card, TruckerController truckerController) throws WrongCallerException, GeneralError {
        ArrayList<Goods> goodsList = card.plotGoodsList(truckerController);
        return new GoodsListResult(card.getCardState(), goodsList);
    }

    public CardStateResult call(WarZone card, ArrayList<Integer> indexes, TruckerController truckerController, Bank bank) throws InvalidGoodChosen, NotEnoughGoodsChosen, WrongCallerException, GeneralError {
        card.removeAsking(indexes, truckerController, bank);
        return new CardStateResult(card.getCardState());
    }

    public IntResult callGetBatteries(WarZone card) throws WrongCallerException, GeneralError {
        int result = card.getNumOfBatteriesToRemove();
        return new IntResult(card.getCardState(), result);
    }

    public CoordinatesResult callGetMeteor(WarZone card) throws WrongCallerException, GeneralError {
        int[] result = card.getCurrentMeteor();
        return new CoordinatesResult(card.getCardState(), result);
    }

    public CardStateResult call(WarZone card, ArrayList<int[]> coordinatesToRemoveBatteries, TruckerController truckerController) throws InvalidCoordinates, WrongNumberOfBatteriesSelected, NotEnoughBatteries, WrongCallerException, GeneralError {
        card.removeBatteries(coordinatesToRemoveBatteries, truckerController);
        return new CardStateResult(card.getCardState());
    }

    public CardStateResult call(WarZone card, boolean choice) throws WrongCallerException, GeneralError {
        card.setAnswers(choice);
        return new CardStateResult(card.getCardState());
    }

    public CardStateResult call(WarZone card, ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, WrongCallerException, GeneralError {
        return new CardStateResult(card.removeCrew(coordinates));
    }

}
