package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;

import java.io.IOException;

public class ClientHandlerSocket implements ClientHandler {
    private final NetworkServer network;
    private final ServerController controller;
    private Thread listenerThread;
    private TruckerController truckerController;

    public ClientHandlerSocket(NetworkServer network, ServerController controller) {
        this.network = network;
        this.controller = controller;
        controller.addClient(this);
        startListener();
    }

    @Override
    public void setTruckerController(TruckerController truckerController) {
        this.truckerController = truckerController;
    }

    @Override
    public TruckerController getTruckerController() {
        return truckerController;
    }

    private void startListener() {
        listenerThread = new Thread(() -> {
            try {
                while (!network.isClosed()) {
                    ClientEvent event = network.receiveMessage();
                    controller.handleEvent(event, this);
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
    public void sendEvent(ServerEvent event) {
        try {

            network.sendMessage(event);
        } catch (IOException e) {
            System.err.println("Error while sending event to the client: " + e.getMessage());
        }
    }

    public void close() {
        try {
            network.close();
        } catch (IOException e) {
            System.err.println("Error while closing connection: " + e.getMessage());
        }
    }
}

