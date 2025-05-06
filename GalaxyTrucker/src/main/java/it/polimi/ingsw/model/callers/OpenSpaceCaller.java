package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.OpenSpace;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.Result;

import java.util.ArrayList;

public class OpenSpaceCaller implements CardCaller{
    public CardStateResult call(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws InvalidCoordinates {
        return new CardStateResult(card.setCoordinates(trucker, truckerCoordinate, numTruckers));
    }
    public CardStateResult call(OpenSpace card, Game game){
        return new CardStateResult(card.cardEffect(game));
    }
}
