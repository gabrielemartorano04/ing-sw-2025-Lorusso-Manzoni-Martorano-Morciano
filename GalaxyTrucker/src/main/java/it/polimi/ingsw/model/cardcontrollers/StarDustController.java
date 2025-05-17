package it.polimi.ingsw.model.cardcontrollers;

import it.polimi.ingsw.controller.CardController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.StarDust;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;

import java.io.Serializable;

/**
 *
 */
public class StarDustController extends CardController implements Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult handle(StarDust card, Game game) throws GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }
}
