package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameState;
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
    private final GameState gameState;
    private final UI ui;
    private Game game;
    private GameController gameController;

    public ViewFlow(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.inputReader = new InputReader();
        this.inputParser = new InputParser();
        this.nickname = "";
        this.isRunning = true;
        this.gameState = GameState.SETUP;
        ui = new TUI();
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
                        statusBuilding();
                    }
                    case FLIGHT -> {
                        statusFlighting();
                    }
                    case ENDED -> {
                        statusEnded();
                    }
                }
            }
        }
    }

    private void statusWaiting() {

    }

    private void statusBuilding() {

    }

    private void statusFlighting() {

    }

    private void statusEnded() {

    }
}

