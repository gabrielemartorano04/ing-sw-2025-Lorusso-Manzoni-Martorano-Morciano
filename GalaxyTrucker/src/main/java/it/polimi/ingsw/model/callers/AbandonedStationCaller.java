package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.AbandonedStation;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidPositionException;
import it.polimi.ingsw.utilities.exceptions.WrongCallerException;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.GoodsResult;

import java.io.Serializable;

/**
 *
 */
public class AbandonedStationCaller implements CardCaller, Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult call(AbandonedStation card, TruckerController truckerController, boolean landing, int numPlayer, Bank bank) throws GeneralError, WrongThreadException {
        return new CardStateResult(card.truckerLanding(truckerController,landing,numPlayer,bank));
    }
    public GoodsResult call(AbandonedStation card, boolean choice) throws GeneralError, WrongCallerException {
        Goods g = card.setChoiceToGain(choice);
        return new GoodsResult(card.getCardState(), g);
    }
    public CardStateResult call(AbandonedStation card, int[] winnerCoordinatesToAddGood, int value, TruckerController truckerController, Bank bank) throws GeneralError, InvalidCoordinates, WrongCallerException {
        return new CardStateResult(card.setWinnerCoordinatesToAddGoods(winnerCoordinatesToAddGood, value, truckerController, bank));
    }
    public CardStateResult call(AbandonedStation card, TruckerController truckerController, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException {
        return new CardStateResult(card.rearrange(truckerController,coord1,coord2,value1,value2,contiune));
    }
    public CardStateResult call(AbandonedStation card, Game game)throws GeneralError, WrongCallerException {
        return new CardStateResult(card.cardEffect(game));
    }
}
