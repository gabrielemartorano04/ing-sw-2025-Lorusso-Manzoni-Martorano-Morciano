package it.polimi.ingsw.net.socket.events.clientevents;

public class SendBooleanEvent extends ClientEvent {
    private boolean answer;
    public SendBooleanEvent(final boolean answer) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.answer = answer;
    }
    public boolean getAnswer() {
        return answer;
    }
    @Override
    public String getMessage() {
        return "Sent answer " + answer;
    }
}
