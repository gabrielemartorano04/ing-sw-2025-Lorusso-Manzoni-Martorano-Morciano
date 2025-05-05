package it.polimi.ingsw.net.socket.events;

public class EndBuildingEvent extends Event{
    String username,message;
    public EndBuildingEvent(String username){
        super(EventType.END_BUILDING);
        this.username=username;
        message=username + "'s spaceship is done";
    }
    public String getUsername(){
        return username;
    }
    public String getMessage(){
        return message;
    }
}
