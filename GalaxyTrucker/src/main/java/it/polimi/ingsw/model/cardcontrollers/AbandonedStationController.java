package it.polimi.ingsw.model.cardcontrollers;

import it.polimi.ingsw.model.Trucker;
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
public class AbandonedStationController implements CardControllerInterface, Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult handle(AbandonedStation card, Trucker trucker, boolean landing, int numPlayer, Bank bank) throws GeneralError, WrongThreadException {
        return new CardStateResult(card.truckerLanding(trucker,landing,numPlayer,bank));
    }
    public GoodsResult handle(AbandonedStation card, boolean choice) throws GeneralError, WrongCallerException {
        Goods g = card.setChoiceToGain(choice);
        return new GoodsResult(card.getCardState(), g);
    }
    public CardStateResult handle(AbandonedStation card, int[] winnerCoordinatesToAddGood, int value, Trucker trucker, Bank bank) throws GeneralError, InvalidCoordinates, WrongCallerException {
        return new CardStateResult(card.setWinnerCoordinatesToAddGoods(winnerCoordinatesToAddGood, value, trucker, bank));
    }
    public CardStateResult handle(AbandonedStation card, Trucker trucker, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException, WrongCallerException {
        return new CardStateResult(card.rearrange(trucker,coord1,coord2,value1,value2,contiune));
    }
    public CardStateResult handle(AbandonedStation card, Game game)throws GeneralError, WrongCallerException {
        return new CardStateResult(card.cardEffect(game));
    }
}
