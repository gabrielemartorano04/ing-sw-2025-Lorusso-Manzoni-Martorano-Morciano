package it.polimi.ingsw.param;

public class Param5<A,B,C,D,E> implements Params {
    private final A first;
    private final B second;
    private final C third;
    private final D fourth;
    private final E fifth;

    private boolean isEmpty=false;

    public Param5(A first, B second, C third, D fourth, E fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
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

    public boolean isEmpty() {
        return isEmpty;
    }
}
