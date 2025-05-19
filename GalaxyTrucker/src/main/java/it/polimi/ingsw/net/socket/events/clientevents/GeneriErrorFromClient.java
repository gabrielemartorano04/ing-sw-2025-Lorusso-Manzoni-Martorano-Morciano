package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class GeneriErrorFromClient extends ClientEvent{
    private String message;
    public GeneriErrorFromClient(String msg){
        super(ClientEventType.SEND_INFO_EVENT);
        this.message=msg;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return null;
    }

}
