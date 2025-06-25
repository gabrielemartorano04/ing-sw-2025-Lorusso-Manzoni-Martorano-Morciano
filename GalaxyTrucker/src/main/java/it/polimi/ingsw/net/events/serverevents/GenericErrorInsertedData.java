package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

/**
 * The GenericErrorInsertedData class represents a specific server event that indicates an error has occurred
 * regarding inserted data
 */
public class GenericErrorInsertedData extends ServerEvent {

    /**
     * Event constructor
     */
    public GenericErrorInsertedData(){
        super(false);
    }

    /**
     * @return the message associated with the error
     */
    @Override
    public String getMessage(){
        return "Error inserted data";
    }

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
