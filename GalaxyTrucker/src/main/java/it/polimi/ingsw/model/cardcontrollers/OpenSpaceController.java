package it.polimi.ingsw.model.cardcontrollers;

import it.polimi.ingsw.model.Trucker;
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
public class OpenSpaceController implements CardController, Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult call(OpenSpace card, Trucker trucker, ArrayList<int[]> truckerCoordinate, int numTruckers) throws GeneralError, InvalidCoordinates {
        return new CardStateResult(card.setCoordinates(trucker, truckerCoordinate, numTruckers));
    }
    public CardStateResult call(OpenSpace card, Game game) throws GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }
}
