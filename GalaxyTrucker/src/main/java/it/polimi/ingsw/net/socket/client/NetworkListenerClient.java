package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.EventReceiver;
import it.polimi.ingsw.view.ViewFlow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Listens for ServerEvent objects from the server over a TCP socket,
 * forwarding received events to the client-side ViewFlow for processing.
 * Runs a dedicated listener thread to continuously read and dispatch events.
 */
public class NetworkListenerClient {

    /** Socket connected to the server */
    private final Socket socket;

    /** Receiver that handles incoming server events */
    private final EventReceiver view;

    /** Thread running the listening loop */
    private Thread listenerThread;


    /** Controls the running state of the listener thread */
    private boolean running;

    /**
     * Constructs a NetworkListenerClient for the given socket and event receiver.
     *
     * @param socket TCP socket connected to the server
     * @param view handler for incoming server events
     */
    public NetworkListenerClient(Socket socket, EventReceiver view) {
        this.socket = socket;
        this.view = view;
    }

    /**
     * Starts the listener thread to receive and dispatch ServerEvent objects
     * from the server until stopped or connection is closed.
     */
    public void start() {
        running = true;
        listenerThread = new Thread(() -> {
            try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
                while (running && !socket.isClosed()) {
                    try {
                        ServerEvent event = (ServerEvent) inputStream.readObject();
                        view.onEventReceived(event);


                    } catch (ClassCastException e) {
                        System.err.println("Error: object received not in ServerEvent. Type: " + e.getMessage());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                if (running) {
                    System.err.println("Error in listener client: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    /**
     * Stops the listener thread and interrupts it if running.
     */
    public void stop() {
        running = false;
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

}
