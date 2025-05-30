package it.polimi.ingsw.net;

import it.polimi.ingsw.controller.TruckerController;

public class Client {
    private final ClientHandler clientHandler;
    private final TruckerController truckerController;

    public Client(ClientHandler clientHandler, TruckerController truckerController) {
        this.clientHandler = clientHandler;
        this.truckerController = truckerController;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public TruckerController getTruckerController() {
        return truckerController;
    }
}
