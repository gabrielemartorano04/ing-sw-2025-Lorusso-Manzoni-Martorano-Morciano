package it.polimi.ingsw.net.rmi.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.ClientHandler;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {
    private final ServerController controller;

    public RmiServer(ServerController controller) throws RemoteException {
        super();
        this.controller = controller;
    }

    @Override
    public void connect(RemoteClientHandler client) throws RemoteException {
        this.controller.addClient(new Client(null, client));
    }

    public static void bind(ServerController serverController) {
        try {
            String localIp = getLocalIPAddress();
            System.setProperty("java.rmi.server.hostname", localIp);
            System.out.println("Local IP for RMI: " + localIp);

            RmiServer server = new RmiServer(serverController);
            Registry registry = LocateRegistry.createRegistry(4321);
            registry.rebind("GalaxyTrucker", server);
            System.out.println("RMI Server bound under name 'GalaxyTrucker' on " + localIp);

        } catch (RemoteException e) {
            System.err.println("Error starting RMI server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleIncomingEvent(ClientEvent event, RemoteClientHandler client) throws RemoteException {
        controller.launchThreadEvent(controller.generateSourceForRmi(event, client.getId()));
    }

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





