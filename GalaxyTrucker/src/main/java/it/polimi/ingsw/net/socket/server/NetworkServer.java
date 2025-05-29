package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkServer {
    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public NetworkServer(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(ServerEvent event) throws IOException {
        outputStream.reset();
        outputStream.writeObject(event);
        outputStream.flush();
    }

    public ClientEvent receiveMessage() throws IOException, ClassNotFoundException {
        ClientEvent event=(ClientEvent)inputStream.readObject();
        if (event!=null) {
            System.out.println("message received from client: "+ event.getMessage());
            return event;
        } else {
            throw new IOException("null event received");
        }
    }

    public void close() throws IOException {
        socket.close();
        inputStream.close();
        outputStream.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}


