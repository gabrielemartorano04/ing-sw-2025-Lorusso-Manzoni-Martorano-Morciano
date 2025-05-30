package it.polimi.ingsw.net;

import it.polimi.ingsw.controller.TruckerController;

public class Client {
    private final ClientHandler clientHandler;
    private TruckerController truckerController;

    public Client(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
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
