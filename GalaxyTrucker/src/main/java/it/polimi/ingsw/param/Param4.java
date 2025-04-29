package it.polimi.ingsw.param;

public class Param4<A,B,C,D> implements Params{
    private final A first;
    private final B second;
    private final C third;
    private final D fourth;

    private boolean isEmpty=false;

    public Param4(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
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
    public D getFourth() {
        return fourth;
    }
    public boolean isEmpty() {
        return isEmpty;
    }
}
