package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.InvalidComponentsException;
import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.visitor.CardVisitor;
import it.polimi.ingsw.model.visitor.ParametricCardVisitor;

import java.util.ArrayList;

public interface MockedAbandonedShip {

    // Metodi di "sola lettura" sui dati della nave
    int getDays();
    int getCredits();
    int getNumHuman();

    // Metodi di interazione (stato e coordinate)
    CardState setTruckerThatHasDecided(Trucker trucker, boolean landing, int numPlayer);
    CardState setCoordinates(ArrayList<int[]> coordinates) throws InvalidComponentsException, InvalidCoordinates;

    // Metodi di effetto gioco
    CardState cardEffect(Game game);

    // Metodi per Visitor pattern
    Object accept(CardVisitor visitor);
    Object accept(ParametricCardVisitor visitor, Object... params) throws InvalidCoordinates;

    String getName();
}
