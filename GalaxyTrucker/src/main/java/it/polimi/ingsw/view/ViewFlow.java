package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ClientHandler;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.view.text.TUI;

public clas
Flow implements NetworkListenerClient.EventReceiver {
    private final ClientHandler clientHandler;
    private final InputReader inputReader;
    private final InputParser inputParser;
    private final String nickname;
    private final boolean isRunning;
    private GameState gameState;
    private BuildingState buildingState;
    private CardState cardState;
    private final UI ui;
    private Trucker trucker;

    public ViewFlow(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.inputReader = new InputReader();
        this.inputParser = new InputParser();
        this.nickname = "";
        this.isRunning = true;
        this.buildingState = null;
        this.cardState = null;
        ui = new TUI();
        new Thread().start();
    }

    @Override
    public void onEventReceived(Event event) {
        System.out.println("[SERVER] Messaggio ricevuto: " + event);
    }

    public void start() {
        System.out.println("SimpleView avviata.");
        while (true) {
            String input = inputReader.readLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            Event event = inputParser.parse(input);
            if (event != null) {
                clientHandler.handleEvent(event);
            }
        }
        System.out.println("Chiusura View.");
    }

    public void run() {
        ui.showWelcome();
        while (!Thread.interrupted()) {
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void statusWaiting() {
        while (!Thread.interrupted()) {

        }
    }

    private void statusBuilding() {
        while (!Thread.interrupted()) {
            if(buildingState != null) {
                switch (buildingState){
                    case BUILDING -> {
                        ui.showBuildOptions();
                    }
                    case SELECT_COMPONENT -> {
                        for (){
                            if (game.getComponents().getComponents().getFirst().getCovered()==false){
                                ui.showAvailableComponent();
                            }
                        }
                    }
                    case SHOW_STACK -> {
                        ui.show
                    }
                    case TURN_HOURGLASS -> {

                    }
                    case LINK -> {

                    }
                    case KEEP -> {
                        ui.showKeptComponents(trucker.getSpaceShip().getKept());
                    }
                    case WAIT_END -> {
                        ui.
                    }
                    case END -> {

                    }
                }
            }
        }
    }

    private void statusFlight() {

    }

    private void statusEnded() {

    }
}

