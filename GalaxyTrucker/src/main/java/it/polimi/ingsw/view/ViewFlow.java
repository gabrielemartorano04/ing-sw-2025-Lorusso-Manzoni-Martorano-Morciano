package it.polimi.ingsw.view;

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
    private final UI ui;
    private volatile boolean running;

    public ViewFlow(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.inputReader = new InputReader();
        this.inputParser = new InputParser();
        this.nickname = "";
        this.running = true;
        ui = new TUI();
        new Thread().start();
    }

    @Override
    public void onEventReceived(Event event) {
        System.out.println("[SERVER] Messaggio ricevuto: " + event);
        //!!questa deve gestire gli eventi dal server!!
        switch (event.getEventType()){
            case LOGIN -> {}
            case END_GAME -> {}
            case NEXT_TURN -> {}
            case LINK_TILE -> {}
            case SHOW_CARD -> {}
            case ASK_BOOLEAN -> {}
            case CREATE_GAME -> {}
            case SELECT_TILE -> {}
            case END_BUILDING -> {}
            case

        }
    }

    public void start() {
        ui.showWelcome();

        // Thread per input utente
        new Thread(() -> {
            while (running) {
                String input = null;
                try {
                    input = inputReader.nextLineBlocking();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (input == null || input.equalsIgnoreCase("exit")) {
                    running = false;
                    break;
                }
                Event event = inputParser.parse(input);
                if (event != null) {
                    clientHandler.handleEvent(event);
                }
            }
        }, "UserInputThread").start();

        // Thread per eventi da server
        new Thread(() -> {
            while (running) {
                // Bloccante o polling da socket
                Event eventFromServer = clientHandler.getNextEventBlocking();
                if (eventFromServer != null) {
                    onEventReceived(eventFromServer);
                }
            }
        }, "ServerListenerThread").start();
    }

    public void run() {
        ui.showWelcome();
        start(); // Il tuo vecchio 'start()' può diventare questo
        while (running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

