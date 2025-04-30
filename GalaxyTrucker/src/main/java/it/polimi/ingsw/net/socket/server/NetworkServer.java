package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.net.socket.events.Event;

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

    public void sendMessage(Event event) throws IOException {
        outputStream.writeObject(event);
        outputStream.flush();
    }

    public Event receiveMessage() throws IOException, ClassNotFoundException {
        Object obj = inputStream.readObject();
        if (obj instanceof Event event) {
            return event;
        } else {
            throw new IOException("Messaggio ricevuto non è un Event");
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


