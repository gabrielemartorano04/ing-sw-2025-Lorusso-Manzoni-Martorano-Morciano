package it.polimi.ingsw.param;

import it.polimi.ingsw.model.cards.CardState;

public final class GenericResult<T> implements Result {
    private final T value;
    private final CardState state;
    private final boolean isVoid;
    public GenericResult(T value, CardState state, boolean isVoid) {
        this.value = value;
        this.state = state;
        this.isVoid = isVoid;
    }

    public T getValue() { return value; }
    public CardState getState() { return state; }
}
