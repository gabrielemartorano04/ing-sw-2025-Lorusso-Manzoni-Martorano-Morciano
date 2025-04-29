package it.polimi.ingsw.param;

public final class GenericResult<T> implements Result {
    private final T value;

    public GenericResult(T value) { this.value = value; }

    public T getValue() { return value; }
}
