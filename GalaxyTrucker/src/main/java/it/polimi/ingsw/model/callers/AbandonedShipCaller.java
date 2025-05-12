package it.polimi.ingsw.model.callers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.exceptions.WrongCallerException;
import it.polimi.ingsw.utilities.exceptions.GeneralError;
import it.polimi.ingsw.utilities.results.CardStateResult;

import java.io.Serializable;
import java.util.ArrayList;

public class AbandonedShipCaller implements CardCaller, Serializable {
    private static final long serialVersionUID = 1L;
    public CardStateResult call(AbandonedShip card, Trucker trucker, boolean landing, int numplayer) throws WrongCallerException, GeneralError {
        return new CardStateResult(card.setTruckerThatHasDecided(trucker, landing, numplayer));
    }
    public CardStateResult call(AbandonedShip card, ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates, WrongCallerException, GeneralError {
        return new CardStateResult(card.setCoordinates(coordinates));
    }
    public CardStateResult call(AbandonedShip card, Game game)throws WrongCallerException, GeneralError {
        return new CardStateResult(card.cardEffect(game));
    }
    // si potrebbe aggiungere il visit per i vari metodi getter ma secondo me non li usiamo mai
}





