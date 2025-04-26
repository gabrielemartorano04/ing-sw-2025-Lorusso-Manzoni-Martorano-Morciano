package it.polimi.ingsw.net.server;

import it.polimi.ingsw.net.events.Event;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandlerServer {
    private final Socket clientSocket;
    private ObjectOutputStream outputStream;
    private Thread listenerThread;
    private final ServerController controller; // Logica di gioco

    public ClientHandlerServer(Socket socket, ServerController controller) {
        this.clientSocket = socket;
        this.controller = controller;
        setup();
    }

    private void setup() {
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            startListener();
        } catch (IOException e) {
            System.err.println("Errore setup client handler: " + e.getMessage());
        }
    }

    private void startListener() {
        listenerThread = new Thread(() -> {
            try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {
                while (!clientSocket.isClosed()) {
                    Object obj = inputStream.readObject();
                    if (obj instanceof Event event) {
                        controller.handleEvent(event, this); // Passa l'evento al controller server
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Errore listener server: " + e.getMessage());
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendEvent(Event event) {
        try {
            outputStream.writeObject(event);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("Errore invio evento a client: " + e.getMessage());
        }
    }
}
