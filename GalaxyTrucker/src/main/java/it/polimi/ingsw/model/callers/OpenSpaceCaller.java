package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.controller.TurnManager;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.OpenSpace;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class OpenSpaceCaller implements CardCaller, Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult call(OpenSpace card, TurnManager.TruckerController truckerController, ArrayList<int[]> truckerCoordinate, int numTruckers) throws GeneralError, InvalidCoordinates {
        return new CardStateResult(card.setCoordinates(truckerController, truckerCoordinate, numTruckers));
    }
    public CardStateResult call(OpenSpace card, Game game) throws GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }
}
