package it.polimi.ingsw.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkServer {
    private final int port;
    private final ConnectionHandlerFactory handlerFactory;

    public NetworkServer(int port, ConnectionHandlerFactory handlerFactory) {
        this.port = port;
        this.handlerFactory = handlerFactory;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server avviato sulla porta " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handlerFactory.create(clientSocket);
            }
        }
    }

    public interface ConnectionHandlerFactory {
        void create(Socket socket);
    }
}
