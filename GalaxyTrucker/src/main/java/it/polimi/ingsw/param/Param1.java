package it.polimi.ingsw.param;

public class Param1<A> implements Params{
    private final A first;

    public Param1(A first) {
        this.first = first;
    }

    public A getFirst() {
        return first;
    }
}
