package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.StarDust;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.Result;

public class StarDustCaller implements CardCaller {
    public CardStateResult call(StarDust card, Game game) throws GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }
}
