package it.polimi.ingsw.net.socket.events.serverevents;

public class NumberOfPlayersError extends ServerEvent{
    public NumberOfPlayersError(){
        super(ServerEventType.PLAYERS_NUMBER_ERROR);

    }
    @Override
    public String getMessage(){
        return "Number of players error. It has to be between 2 and 4!";
    }
}
