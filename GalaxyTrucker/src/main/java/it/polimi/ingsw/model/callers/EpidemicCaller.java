package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Epidemic;
import it.polimi.ingsw.utilities.exceptions.WrongCallerException;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;

public class EpidemicCaller implements CardCaller{
    @Override
    public CardStateResult call(Epidemic card, Game game) throws WrongCallerException, GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }
}
