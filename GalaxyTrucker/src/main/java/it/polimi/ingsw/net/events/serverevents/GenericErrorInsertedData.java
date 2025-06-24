package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

/**
 * Error event from server to client. It signals a generic error when the player inserted data
 */
public class GenericErrorInsertedData extends ServerEvent {
    private String message;
    public GenericErrorInsertedData(){
        super(false);
        message = "Error inserted data";
    }
    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
