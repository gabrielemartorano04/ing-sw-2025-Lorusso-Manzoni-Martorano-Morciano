package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.io.IOException;
import java.rmi.RemoteException;

public class ClientHandlerSocket implements ClientHandler {
    private final NetworkServer network;
    private final ServerController controller;
    private int id;

    public ClientHandlerSocket(NetworkServer network, ServerController controller) {
        this.network = network;
        this.controller = controller;
        this.id = -1;
        startListener();
    }

    private void startListener() {
        Thread listenerThread = new Thread(() -> {
            try {
                while (!network.isClosed()) {
                    ClientEvent event = network.receiveMessage();
                    controller.addEventToQueue(event, this);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error receiving the event: " + e.getMessage());
            } finally {
                System.out.println("Client disconnected.");
                controller.removeClient(this);
                close();
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    @Override
    public void handleEvent(ServerEvent event) {
        try {

            network.sendMessage(event);
        } catch (IOException e) {
            System.err.println("Error while sending event to the client: " + e.getMessage());
        }
    }

    @Override
    public int getId() throws RemoteException {
        return id;
    }

    @Override
    public void setId(int id) throws RemoteException {
        this.id = id;
    }

    public void close() {
        try {
            network.close();
        } catch (IOException e) {
            System.err.println("Error while closing connection: " + e.getMessage());
        }
    }
}

