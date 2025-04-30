package it.polimi.ingsw.utilities.params;

public class Param2<A,B> implements Params{
    private final A first;
    private final B second;
    private boolean isEmpty=false;

    public Param2(A first, B second) {
        this.first = first;
        this.second = second;
    }
    public A getFirst() {
        return first;
    }
    public B getSecond() {
        return second;
    }
    public boolean isEmpty() {
        return isEmpty;
    }
}
