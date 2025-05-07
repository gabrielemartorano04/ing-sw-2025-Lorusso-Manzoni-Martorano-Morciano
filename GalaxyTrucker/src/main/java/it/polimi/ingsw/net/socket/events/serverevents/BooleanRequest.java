package it.polimi.ingsw.net.socket.events.serverevents;

public class BooleanRequest extends ServerEvent {
    private String question;
    private int userId;

    public BooleanRequest(ServerEventType eventType, String question, int Id) {
        super(ServerEventType.REQUEST);
        this.question = question;
        this.userId = Id;
    }
    @Override
    public String getMessage() {
        return question;
    }

    public int getUserId() {
        return userId;
    }


}
