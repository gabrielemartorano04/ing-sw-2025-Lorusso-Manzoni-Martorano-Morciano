package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NetworkListenerClient {
    private final Socket socket;
    private final EventReceiver receiver;
    private Thread listenerThread;
    private boolean running;

    public NetworkListenerClient(Socket socket, EventReceiver receiver) {
        this.socket = socket;
        this.receiver = receiver;
    }

    public void start() {
        running = true;
        listenerThread = new Thread(() -> {
            try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
                while (running && !socket.isClosed()) {
                    Object obj = inputStream.readObject();
                    if (obj instanceof ClientEvent event) {
                        receiver.onEventReceived(event);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                if (running) {
                    System.err.println("Errore nel listener client: " + e.getMessage());
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void stop() {
        running = false;
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    public interface EventReceiver {
        void onEventReceived(ClientEvent event);
    }
}
