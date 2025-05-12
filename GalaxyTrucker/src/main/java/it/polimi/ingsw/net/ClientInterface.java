package it.polimi.ingsw.net;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public interface ClientInterface {
    public void connect() throws IOException;

    public void sendMessage(ClientEvent event) throws IOException;

    public void disconnect() ;
}
