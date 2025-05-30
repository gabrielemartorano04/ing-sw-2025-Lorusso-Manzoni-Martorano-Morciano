package it.polimi.ingsw.net.events.clientevents;

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
    public void call(VirtualServer visitor) {

    }

}
