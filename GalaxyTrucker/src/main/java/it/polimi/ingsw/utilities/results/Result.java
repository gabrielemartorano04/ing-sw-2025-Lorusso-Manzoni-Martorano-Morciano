package it.polimi.ingsw.utilities.results;

import it.polimi.ingsw.model.cards.CardState;

/**
 * interface used to handle all the possible results coming from all metods implemented in the cards
 */
public interface Result {
    public CardState getState();
}

