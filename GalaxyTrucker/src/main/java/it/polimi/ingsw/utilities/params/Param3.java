package it.polimi.ingsw.utilities.params;

public class Param3<A,B,C> implements Params{
    private final A first;
    private final B second;
    private final C third;
    private boolean isEmpty=false;

    public Param3(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    public A getFirst() {
        return first;
    }
    public B getSecond() {
        return second;
    }
    public C getThird() {
        return third;
    }
    public boolean isEmpty() {
        return isEmpty;
    }
}
