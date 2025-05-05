package it.polimi.ingsw.net.socket.events;

/**
 * Event from client to server. The player wants to select a component through a given index
 */
public class SelectTileEvent extends Event {

    private String message,username;
    private int index;

    public SelectTileEvent(String username, int index) {
        super(EventType.SELECT_TILE);
        this.username = username;
        this.index = index;
        message = username + " select component " + index;
    }

    public String getMessage() {
        return message;
    }
    public String getUsername() {
        return username;
    }
    public int getIndex() {
        return index;
    }
}
