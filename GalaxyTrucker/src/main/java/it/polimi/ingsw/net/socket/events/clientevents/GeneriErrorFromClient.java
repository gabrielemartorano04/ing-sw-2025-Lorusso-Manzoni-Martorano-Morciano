package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class GeneriErrorFromClient extends ClientEvent{
    private String message;
    public GeneriErrorFromClient(String msg){
        this.message=msg;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ServerEvent call(VirtualServer visitor) {
        return null;
    }

}
