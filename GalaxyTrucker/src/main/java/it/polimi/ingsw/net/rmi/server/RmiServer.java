package it.polimi.ingsw.net.rmi.server;

import it.polimi.ingsw.net.control.ServerController;
import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

/**
 * RMI server implementation exposing remote methods for client connections and event handling.
 * Binds itself to the RMI registry allowing clients to connect and send events.
 * Retrieves the local IP address to properly configure the RMI hostname.
 */
public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {

    /**
     * controller of the Server that handles all clients
     */
    private final ServerController controller;

    /**
     * Constructs and exports the RMI server with the given controller.
     *
     * @param controller the main server controller handling game logic
     * @throws RemoteException if export fails
     */
    public RmiServer(ServerController controller) throws RemoteException {
        super();
        this.controller = controller;
    }

    /**
     * Registers a new client by wrapping the remote client handler.
     *
     * @param client remote client callback stub
     * @throws RemoteException if remote invocation fails
     */
    @Override
    public void connect(RemoteClientHandler client) throws RemoteException {
        this.controller.addClient(new Client(null, client));
    }

    /**
     * Handles client events by launching a dedicated thread for processing.
     *
     * @param event client event to handle
     * @param client remote client that sent the event
     * @throws RemoteException if remote invocation fails
     */
    @Override
    public void handleIncomingEvent(ClientEvent event, RemoteClientHandler client) throws RemoteException {
        controller.launchThreadEvent(controller.generateSourceForRmi(event, client.getId()));
    }

    /**
     * Binds the server instance to the RMI registry on port 4321 with a specific name.
     * Sets the RMI system property to the local IP address.
     *
     * @param serverController the controller to associate with the server
     * @return the bound RmiServer instance or null if binding fails
     */
    public static RmiServer bind(ServerController serverController) {
        try {
            String localIp = getLocalIPAddress();
            System.setProperty("java.rmi.server.hostname", localIp);
            System.out.println("Local IP for RMI: " + localIp);

            RmiServer server = new RmiServer(serverController);
            Registry registry = LocateRegistry.createRegistry(4321);
            registry.rebind("GalaxyTrucker", server);
            System.out.println("RMI Server bound under name 'GalaxyTrucker' on " + localIp);

            return server;
        } catch (RemoteException e) {
            System.err.println("Error starting RMI server: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the first available non-loopback IPv4 address of the local machine.
     *
     * @return local IPv4 address or "127.0.0.1" if none found
     */
    private static String getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

}





