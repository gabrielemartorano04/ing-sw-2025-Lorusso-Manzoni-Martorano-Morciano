package it.polimi.ingsw.net;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

import java.io.IOException;

public interface ServerInterface {
    public void sendMessage(ServerEvent event) throws IOException;
}
