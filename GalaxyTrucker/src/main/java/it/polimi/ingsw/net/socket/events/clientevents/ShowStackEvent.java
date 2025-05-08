package it.polimi.ingsw.net.socket.events.clientevents;

public class ShowStackEvent extends ClientEvent {
    private int index;
    public ShowStackEvent(final int index) {
        super(ClientEventType.SHOW_STACK, false);
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    @Override
    public String getMessage() {
        return "I want to stanch n " + index;
    }
}
