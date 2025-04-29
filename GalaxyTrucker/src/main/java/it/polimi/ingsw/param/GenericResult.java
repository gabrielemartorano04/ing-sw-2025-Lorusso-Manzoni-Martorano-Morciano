package it.polimi.ingsw.param;

import it.polimi.ingsw.model.cards.CardState;

public final class GenericResult<T> implements Result {
    private final T value;
    private final CardState state;
    public GenericResult(T value, CardState state) {
        this.value = value;
        this.state = state;
    }

    public T getValue() { return value; }
    public CardState getState() { return state; }
}
