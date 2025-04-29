package it.polimi.ingsw.param.results;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.components.Goods;

public class GoodsArrayResult implements Result {
    private final CardState state;
    private final Goods[] result;
    public GoodsArrayResult(CardState state, Goods[] result) {
        this.state = state;
        this.result = result;
    }
    public CardState getState() {
        return state;
    }
    public Goods[] getResult() {
        return result;
    }
}
