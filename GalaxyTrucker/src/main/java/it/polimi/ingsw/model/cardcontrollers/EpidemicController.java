package it.polimi.ingsw.model.cardcontrollers;

import it.polimi.ingsw.controller.CardController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Epidemic;
import it.polimi.ingsw.utilities.exceptions.WrongCallerException;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;

import java.io.Serializable;

/**
 *
 */
public class EpidemicController extends CardController implements Serializable {
    private static final long serialVersionUID = 1L;
    @Override
    public CardStateResult handle(Epidemic card, Game game) throws WrongCallerException, GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }
}
