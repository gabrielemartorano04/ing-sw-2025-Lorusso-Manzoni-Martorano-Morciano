package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Planets;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidPositionException;
import it.polimi.ingsw.utilities.exceptions.PlanetNotFreeException;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.CoordinatesResult;
import it.polimi.ingsw.utilities.results.GoodsResult;

import java.io.Serializable;

/**
 *
 */
public class PlanetsCaller implements CardCaller , Serializable {
    private static final long serialVersionUID = 1L;
    public CoordinatesResult callGetBank(Planets card, Bank bank) throws GeneralError {
        int[] result = card.getBank(bank);
        return new CoordinatesResult(card.getState(), result);
    }

    public CardStateResult CallInitialize(Planets card, Bank bank) throws GeneralError {
        return new CardStateResult(card.initialize(bank));
    }

    public CardStateResult call(Planets card, int indexOfLanding, Trucker trucker, int numPlayers) throws PlanetNotFreeException, GeneralError {
        return new CardStateResult(card.setPlayerLanding(indexOfLanding, trucker, numPlayers));
    }

    public GoodsResult call(Planets card) throws GeneralError {
        if(card.getState() == CardState.PLOT_GOOD){
            Goods g = card.plotGoodFromPlanet();
            return new GoodsResult(card.getState(),g);
        } else {
            Goods g = card.plotGoodFromList();
            return new GoodsResult(card.getState(),g);
        }
    }

    public CardStateResult call(Planets card, boolean choice) throws GeneralError {
        if(card.getState() == CardState.CHOOSE_TO_GAIN){
            return new CardStateResult(card.chooseToGain(choice));
        }else{
            return new CardStateResult(card.chooseFromList(choice));
        }
    }

    public CardStateResult call(Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws GeneralError, InvalidCoordinates {
        if (card.getState() == CardState.GOODSTOADD) {
            return new CardStateResult(card.setCoordinatesToAddGoods(coordinatesToAddGood, positionInLoad, bank));
        } else {
            return new CardStateResult(card.coordinatesToAddFromList(coordinatesToAddGood, positionInLoad, bank));
        }
    }

    public CardStateResult call(Planets card, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws GeneralError, InvalidCoordinates, InvalidPositionException{
        return new CardStateResult(card.rearrange(coord1, coord2, value1, value2, contiune));
    }

    public CardStateResult call(Planets card, Game game) throws GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }

}
