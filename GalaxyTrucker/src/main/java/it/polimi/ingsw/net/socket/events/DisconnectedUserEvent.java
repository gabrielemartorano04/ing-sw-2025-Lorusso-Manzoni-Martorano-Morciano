package it.polimi.ingsw.net.socket.events;

public class DisconnectedUserEvent extends Event{
    String username, message;
    public DisconnectedUserEvent(String username){
        super(EventType.DISCONNECTED_USER);
        this.username = username;
        message = username+" disconnected";
    }
    public String getUsername(){
        return username;
    }
    public String getMessage(){
        return message;
    }
}
