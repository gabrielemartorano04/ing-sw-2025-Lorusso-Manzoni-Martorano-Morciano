package it.polimi.ingsw.utilities.params;

public class Param1<A> implements Params{
    private final A first;
    private boolean isEmpty=false;

    public Param1(A first) {
        this.first = first;
    }

    public A getFirst() {
        return first;
    }
    public boolean isEmpty() {
        return isEmpty;
    }
}
