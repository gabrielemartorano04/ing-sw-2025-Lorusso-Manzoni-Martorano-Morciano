package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.ClientInterface;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * socket TCP, invia/riceve messaggi
 * listener che riceve eventi dal server e li inoltra alla View/Controller
 */


public class NetworkClient {
    private final String serverAddress;
    private final int serverPort;
    private Socket socket;
    private ObjectOutputStream outputStream;

    public NetworkClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void sendMessage(ClientEvent event) throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Inactive connection");
        }
        outputStream.writeObject(event);
        outputStream.flush();
    }

    public Socket getSocket() {
        return socket;
    }

    public void disconnect() {
        try {
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error while closing client: " + e.getMessage());
        }
    }
}
