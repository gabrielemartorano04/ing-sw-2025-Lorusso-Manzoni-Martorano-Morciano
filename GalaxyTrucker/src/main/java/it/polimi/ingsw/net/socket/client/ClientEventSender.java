package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;

import java.io.IOException;

/**
 * Receives client events from the view and sends them to the server via the network.
 */
public class ClientEventSender {

    /**
     * The network client used for communication.
     */
    private final NetworkClient network;

    /**
     * Constructs the sender with the given network client.
     *
     * @param network the network client used to send messages
     */
    public ClientEventSender(NetworkClient network) {
        this.network = network;
    }

    /**
     * Sends a client event to the server, handling any I/O errors.
     *
     * @param event the client event to send
     */
    public void handleEvent(ClientEvent event) {
        try {
            network.sendMessage(event);
        } catch (IOException e) {
            System.err.println("Error while sending client event: " + e.getMessage());
        }
    }
}

