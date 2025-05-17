package it.polimi.ingsw.model.cardcontrollers;

import it.polimi.ingsw.controller.CardController;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Slavers;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class SlaversController extends CardController implements Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult handle(Slavers card, Trucker trucker, ArrayList<int[]> coord, int numPlayer) throws InvalidCoordinates, GeneralError, InvalidComponentsException {
        if(card.getCardState() == CardState.HUMANSTOREMOVE){
            return new CardStateResult(card.addTruckersCoordinatesToRemoveHumans(trucker, coord, numPlayer));
        }else{
            return new CardStateResult(card.addDoubleCannonsCoord(trucker, coord, numPlayer));
        }
    }

    public CardStateResult handle(Slavers card, boolean choice) throws GeneralError {
        return new CardStateResult(card.setChooseToGain(choice));
    }

    public CardStateResult handle(Slavers card, Game game) throws GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }

}
