package it.polimi.ingsw.utilities.results;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;

public class GoodsListResult implements Result{

    private final CardState state;
    private final ArrayList<Goods> result;
    public GoodsListResult(CardState state, ArrayList<Goods> result) {
        this.state = state;
        this.result = result;
    }
    public CardState getState() {
        return state;
    }
    public ArrayList<Goods> getResult() {
        return result;
    }
}
