package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * TCP socket client that manages connection to the server,
 * sends client events, and provides access to the socket.
 */
public class NetworkClient {

    /** Server IP address or hostname to connect to */
    private final String serverAddress;

    /** Server port number */
    private final int serverPort;

    /** Socket used for TCP communication */
    private Socket socket;

    /** Output stream to send serialized client events */
    private ObjectOutputStream outputStream;

    /**
     * Constructs a NetworkClient with the specified server address and port.
     *
     * @param serverAddress the server's IP address or hostname
     * @param serverPort the server's port number
     */
    public NetworkClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * Establishes a TCP connection to the server and initializes the output stream.
     *
     * @throws IOException if an I/O error occurs when creating the socket or stream
     */
    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Sends a client event to the server via the output stream.
     *
     * @param event the client event to send
     * @throws IOException if the connection is inactive or sending fails
     */
    public void sendMessage(ClientEvent event) throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Inactive connection");
        }
        outputStream.reset();
        outputStream.writeObject(event);
        outputStream.flush();
    }

    /**
     * @return the socket connected to the server
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Closes the output stream and the socket, releasing network resources.
     * Logs any errors encountered during closing.
     */
    public void disconnect() {
        try {
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error while closing client: " + e.getMessage());
        }
    }
}
