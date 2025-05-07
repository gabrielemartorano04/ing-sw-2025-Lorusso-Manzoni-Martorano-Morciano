package it.polimi.ingsw.net.socket.events.serverevents;

/**
 * Error event from server to client. It signals a generic error when the player inserted data
 */
public class GenericErrorInsertedData extends ServerEvent {
    private String message;
    public GenericErrorInsertedData(){
        super(ServerEventType.GENERIC_ERROR_INSERTED_DATA);
        message = "Error inserted data";
    }
    public String getMessage(){
        return message;
    }
}
