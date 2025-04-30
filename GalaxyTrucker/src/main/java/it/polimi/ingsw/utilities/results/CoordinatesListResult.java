package it.polimi.ingsw.utilities.results;

import it.polimi.ingsw.model.cards.CardState;

import java.util.ArrayList;

public class CoordinatesListResult implements Result{
    private final CardState state;
    private final ArrayList<int[]> result;
    public CoordinatesListResult(CardState state, ArrayList<int[]> result) {
        this.state = state;
        this.result = result;
    }
    public CardState getState() {
        return state;
    }
    public ArrayList<int[]> getResult() {
        return result;
    }
}
