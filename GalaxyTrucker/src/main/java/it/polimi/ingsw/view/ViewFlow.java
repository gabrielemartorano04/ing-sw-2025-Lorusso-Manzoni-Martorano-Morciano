package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.BuildingState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.net.socket.client.ClientHandler;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.view.text.TUI;

public class ViewFlow implements NetworkListenerClient.EventReceiver {
    private final ClientHandler clientHandler;
    private final InputReader inputReader;
    private final InputParser inputParser;
    private final String nickname;
    private final boolean isRunning;
    private GameState gameState;
    private BuildingState buildingState;
    private CardState cardState;
    private final UI ui;
    private Game game;
    private GameController gameController;
    private Trucker trucker;

    public ViewFlow(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.inputReader = new InputReader();
        this.inputParser = new InputParser();
        this.nickname = "";
        this.isRunning = true;
        this.gameState = GameState.SETUP;
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
        game.setGameState(GameState.SETUP);
        while (!Thread.interrupted()) {
            if (gameState != null) {
                switch (game.getGameState()) {
                    case WAITING -> {
                        statusWaiting();
                    }
                    case BUILDING -> {
                        this.buildingState = BuildingState.BUILDING;
                        statusBuilding();
                    }
                    case FLIGHT -> {
                        statusFlight();
                    }
                    case ENDED -> {
                        statusEnded();
                    }
                }
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void statusWaiting() {
        game.setGameState(GameState.WAITING);
        while (!Thread.interrupted()) {

        }
    }

    private void statusBuilding() {
        game.setGameState(GameState.BUILDING);
        while (!Thread.interrupted()) {
            if(buildingState != null) {
                switch (trucker.getBuildingState()){
                    case BUILDING -> {

                    }
                    case SELECT_COMPONENT -> {

                    }
                    case SHOW_STACK -> {

                    }
                    case TURN_HOURGLASS -> {

                    }
                    case LINK -> {

                    }
                    case KEEP -> {

                    }
                    case WAIT_END -> {

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

