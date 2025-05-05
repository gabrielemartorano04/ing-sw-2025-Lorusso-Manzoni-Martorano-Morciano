package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.OpenSpace;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.Result;

import java.util.ArrayList;

public class OpenSpaceVisitor {
    public Result visit(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws InvalidCoordinates {
        return new CardStateResult(card.setCoordinates(trucker, truckerCoordinate, numTruckers));
    }
    public Result visit(OpenSpace card, Game game){
        return new CardStateResult(card.cardEffect(game));
    }
}
