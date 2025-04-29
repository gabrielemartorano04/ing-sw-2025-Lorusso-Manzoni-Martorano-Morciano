package it.polimi.ingsw.param;

public class Param6<A,B,C,D,E,F> implements Params {
    private final A first;
    private final B second;
    private final C third;
    private final D fourth;
    private final E fifth;
    private final F sixth;
    private boolean isEmpty=false;

    public Param6(A first, B second, C third, D fourth, E fifth,F sixth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
        this.sixth = sixth;
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
    public E getFifth() {
        return fifth;
    }
    public F getSixth() {
        return sixth;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
