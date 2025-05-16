package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.StarDust;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;

import java.io.Serializable;

/**
 *
 */
public class StarDustController implements CardController, Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult call(StarDust card, Game game) throws GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }
}
