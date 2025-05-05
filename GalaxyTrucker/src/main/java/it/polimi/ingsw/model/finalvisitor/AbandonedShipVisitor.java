package it.polimi.ingsw.model.finalvisitor;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.AbandonedShip;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.utilities.exceptions.InvalidComponentsException;
import it.polimi.ingsw.utilities.exceptions.InvalidCoordinates;
import it.polimi.ingsw.utilities.results.CardStateResult;
import it.polimi.ingsw.utilities.results.Result;

import java.util.ArrayList;

public class AbandonedShipVisitor {
    public Result visit(AbandonedShip card, Trucker trucker, boolean landing, int numplayer) {
        return new CardStateResult(card.setTruckerThatHasDecided(trucker, landing, numplayer));
    }
    public Result visit(AbandonedShip card, ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates {
        return new CardStateResult(card.setCoordinates(coordinates));
    }
    public Result visit(AbandonedShip card, Game game) {
        return new CardStateResult(card.cardEffect(game));
    }
    // si potrebbe aggiungere il visit per i vari metodi getter ma secondo me non li usiamo mai
}



