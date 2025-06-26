package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.net.control.ServerController;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.io.IOException;

/**
 * Handles communication with a single client over a socket connection.
 * Listens for incoming ClientEvents and forwards them to the ServerController.
 * Also sends ServerEvents to the client and manages connection closure.
 */
public class ClientHandlerSocket implements ClientHandlerSocketInterface {

    /** NetworkServer instance managing socket communication with the client */
    private final NetworkServer network;

    /** ServerController that processes received client events */
    private final ServerController controller;

    /** Unique identifier for this client handler equals to the client id*/
    private int id;

    /**
     * Constructs a ClientHandlerSocket with the given network and controller.
     * Starts the listener thread immediately.
     *
     * @param network the NetworkServer handling socket I/O with the client
     * @param controller the ServerController to dispatch received events to
     */
    public ClientHandlerSocket(NetworkServer network, ServerController controller) {
        this.network = network;
        this.controller = controller;
        this.id = -1;
        startListener();
    }

    /**
     * Starts a background thread that continuously listens for ClientEvents
     * from the client and forwards them to the ServerController.
     * Handles disconnection and resource cleanup.
     */
    private void startListener() {
        Thread listenerThread = new Thread(() -> {
            try {
                while (!network.isClosed()) {
                    ClientEvent event = network.receiveMessage();
                    controller.addEventToQueue(event, this.getId());
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error receiving the event: " + e.getMessage());
            } finally {
                System.out.println("Client disconnected.");
                close();

            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    /**
     * Sets the unique client identifier if not already set.
     *
     * @param id the client ID to assign
     */
    @Override
    public void setId(int id) {
        if(this.id==-1){
            this.id = id;
        }
    }

    /**
     * Returns the unique client identifier.
     *
     * @return the client ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Sends a ServerEvent to the client through the network.
     *
     * @param event the ServerEvent to send
     */
    @Override
    public void sendEvent(ServerEvent event) {
        try {
            network.sendMessage(event);
        } catch (IOException e) {
            System.err.println("Error while sending event to the client: " + e.getMessage());
        }
    }

    /**
     * Closes the network connection to the client, releasing resources.
     */
    @Override
    public void close() {
        try {
            network.close();
        } catch (IOException e) {
            System.err.println("Error while closing connection: " + e.getMessage());
        }
    }


}

