package it.polimi.ingsw.net;

import it.polimi.ingsw.controller.TruckerController;

import java.rmi.RemoteException;

public class Client {
    private final ClientHandler clientHandler;
    private TruckerController truckerController;
    private int id;

    public Client(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setId(int id) {
        this.id = id;
        try {
            this.clientHandler.setId(id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }


    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public TruckerController getTruckerController() {
        return truckerController;
    }

    public void setTruckerController(TruckerController truckerController) {
        this.truckerController = truckerController;
    }
}
