package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Planets;
import it.polimi.ingsw.model.components.Goods;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.InvalidPositionException;
import it.polimi.ingsw.utilities.exceptions.PlanetNotFreeException;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.CoordinatesResult;
import it.polimi.ingsw.utilities.results.GoodsResult;
import it.polimi.ingsw.utilities.results.Result;
import it.polimi.ingsw.view.UI;

public class PlanetsVisitor {
    public Result visit(Planets card, Bank bank){
        if(card.getState() == CardState.PLOT_BANK){
            int[] result = card.getBank(bank);
            return new CoordinatesResult(card.getState(), result);
        }else{
            return new CardStateResult(card.initialize(bank));
        }
    }

    public Result visit(Planets card, int indexOfLanding, Trucker trucker, int numPlayers) throws PlanetNotFreeException {
        return new CardStateResult(card.setPlayerLanding(indexOfLanding, trucker, numPlayers));
    }

    public Result visit(Planets card){
        if(card.getState() == CardState.PLOT_GOOD){
            Goods g = card.plotGoodFromPlanet();
            return new GoodsResult(card.getState(),g);
        } else {
            Goods g = card.plotGoodFromList();
            return new GoodsResult(card.getState(),g);
        }
    }

    public Result visit(Planets card, boolean choice){
        if(card.getState() == CardState.CHOOSE_TO_GAIN){
            return new CardStateResult(card.chooseToGain(choice));
        }else{
            return new CardStateResult(card.chooseFromList(choice));
        }
    }

    public Result visit (Planets card, int[] coordinatesToAddGood, int positionInLoad, Bank bank) throws IllegalStateException, InvalidCoordinates {
        if (card.getState() == CardState.GOODSTOADD) {
            return new CardStateResult(card.setCoordinatesToAddGoods(coordinatesToAddGood, positionInLoad, bank));
        } else {
            return new CardStateResult(card.coordinatesToAddFromList(coordinatesToAddGood, positionInLoad, bank));
        }
    }

    public Result visit (Planets card, int[] coord1, int[] coord2, int value1, int value2, boolean contiune) throws IllegalStateException, InvalidCoordinates, InvalidPositionException{
        return new CardStateResult(card.rearrange(coord1, coord2, value1, value2, contiune));
    }

    public Result visit(Planets card, Game game){
        return new CardStateResult(card.cardEffect(game));
    }

}
