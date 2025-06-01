package it.polimi.ingsw.net;

import it.polimi.ingsw.controller.TruckerController;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;

import java.rmi.RemoteException;

public class Client {
    private final ClientHandler clientHandler;
    private final RemoteClientHandler remoteClientHandler;
    private TruckerController truckerController;
    private int id;

    public Client(ClientHandler clientHandler, RemoteClientHandler remoteClientHandler) {
        this.clientHandler = clientHandler;
        this.remoteClientHandler = remoteClientHandler;
        this.id = -1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public ClientHandler getSocketClient() {
        return clientHandler;
    }

    public RemoteClientHandler getRmiClient() {
        return remoteClientHandler;
    }

    public TruckerController getTruckerController() {
        return truckerController;
    }

    public void setTruckerController(TruckerController truckerController) {
        this.truckerController = truckerController;
    }

    public void updateClient(ServerEvent event){
        if(clientHandler != null){
            clientHandler.sendEvent(event);
        }else if (remoteClientHandler != null){
            try {
                remoteClientHandler.handleIncomingEvent(event);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            System.out.println("Both remoteClientHandler and clientHandler are null!");
        }
    }
}
