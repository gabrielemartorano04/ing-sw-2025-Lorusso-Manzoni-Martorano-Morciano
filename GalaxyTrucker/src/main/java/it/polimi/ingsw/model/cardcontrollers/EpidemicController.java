package it.polimi.ingsw.model.cardcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Epidemic;
import it.polimi.ingsw.utilities.exceptions.WrongCallerException;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;

import java.io.Serializable;

/**
 *
 */
public class EpidemicController implements CardController, Serializable {
    private static final long serialVersionUID = 1L;
    @Override
    public CardStateResult call(Epidemic card, Game game) throws WrongCallerException, GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }
}
