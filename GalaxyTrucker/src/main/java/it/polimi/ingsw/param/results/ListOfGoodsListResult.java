package it.polimi.ingsw.param.results;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;

public class ListOfGoodsListResult implements Result{
    private final CardState state;
    private final ArrayList<ArrayList<Goods>> result;
    public ListOfGoodsListResult(CardState state, ArrayList<ArrayList<Goods>> result) {
        this.state = state;
        this.result = result;
    }
    public CardState getState() {
        return state;
    }
    public ArrayList<ArrayList<Goods>> getResult() {
        return result;
    }
}
