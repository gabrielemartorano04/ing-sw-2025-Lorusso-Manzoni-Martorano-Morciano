package it.polimi.ingsw.net.socket.server;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Methods to manage TCP socket communication with a single client on the server side.
 * Handles sending ServerEvent objects to the client and receiving ClientEvent objects from the client.
 */
public class NetworkServer {

    /** TCP socket connected to the client */
    private final Socket socket;

    /** Output stream to send serialized ServerEvent objects */
    private final ObjectOutputStream outputStream;

    /** Input stream to receive serialized ClientEvent objects */
    private final ObjectInputStream inputStream;

    /** Queue of events to be sent to client */
    private final BlockingQueue<ServerEvent> eventQueue = new LinkedBlockingQueue<>();

    /** Dedicated thread for writing events */
    private Thread writerThread;

    /**
     * Constructs a NetworkServer instance wrapping the given client socket,
     * initializing the input and output streams.
     *
     * @param socket the TCP socket connected to the client
     * @throws IOException if an I/O error occurs when creating streams
     */
    public NetworkServer(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        startWriterThread();
    }

    /**
     * Starts the dedicated thread that sends ServerEvents asynchronously
     * from the eventQueue to the client.
     */
    private void startWriterThread() {
        writerThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                    ServerEvent event = eventQueue.take();
                    outputStream.reset();
                    outputStream.writeUnshared(event);
                    outputStream.flush();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                System.err.println("Error sending event to client: " + e.getMessage());
            }
        }, "NetworkServer-WriterThread");
        writerThread.setDaemon(true);
        writerThread.start();
    }

    /**
     * Sends a ServerEvent to the client.
     * Resets the stream before writing to avoid serialization cache issues.
     *
     * @param event the ServerEvent to send
     * @throws IOException if an I/O error occurs while sending
     */
    public void sendMessage(ServerEvent event) throws IOException {
        eventQueue.offer(event);
    }

    /**
     * Receives a ClientEvent from the client.
     *
     * @return the ClientEvent received from the client
     * @throws IOException if an I/O error occurs or if a null event is received
     * @throws ClassNotFoundException if the ClientEvent class cannot be found during deserialization
     */
    public ClientEvent receiveMessage() throws IOException, ClassNotFoundException {
        ClientEvent event=(ClientEvent)inputStream.readObject();
        if (event!=null) {
            return event;
        } else {
            throw new IOException("null event received");
        }
    }

    /**
     * Closes the socket and associated input/output streams, releasing all resources.
     *
     * @throws IOException if an I/O error occurs during closing
     */
    public void close() throws IOException {
        socket.close();
        inputStream.close();
        outputStream.close();
    }

    /**
     * Checks if the underlying socket is closed.
     *
     * @return true if the socket is closed, false otherwise
     */
    public boolean isClosed() {
        return socket.isClosed();
    }
}


