package it.polimi.ingsw.model.cardcontrollers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.MeteorShower;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.CoordinatesResult;

import java.io.Serializable;

/**
 *
 */
public class MeteorShowerController implements CardController, Serializable {
    private static final long serialVersionUID = 1L;
    public CoordinatesResult call(MeteorShower card) throws GeneralError {
        int[] result = card.getCurrentMeteor();
        return new CoordinatesResult(card.getCardState(), result);
    }
    public CardStateResult call(MeteorShower card, Game game) throws GeneralError {
        CardStateResult result = null;
        if(card.getCardState() == CardState.ROLL_DICE){
            result =  new CardStateResult(card.rollDice(game));
        } else if (card.getCardState() == CardState.ESECUZIONE){
            result = new CardStateResult(card.cardEffect(game));
        }
        return result;
    }
    public CardStateResult call(MeteorShower card, boolean answer) throws GeneralError {
        return new CardStateResult(card.setAnswers(answer));
    }
}
