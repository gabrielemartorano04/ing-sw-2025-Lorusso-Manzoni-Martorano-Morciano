package it.polimi.ingsw.view;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ClientHandler;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.text.TUI;

import java.util.Map;

public class ViewFlow implements NetworkListenerClient.EventReceiver {
    private final ClientHandler clientHandler;
    private final InputReader inputReader;
    private final InputParser inputParser;
    private final UI ui;
    private final String nickname;
    private Component[][] ship;
    private int position;
    private int batteryCount;
    private int enginePower;
    private int cannonPower;
    private int humanCount;
    private Map<String, Integer> goodsByColor;
    private volatile boolean running;
    private ViewState viewState;
    public enum ViewState {
        START,
        BULDING,
        FLIGTH,
        END;
    }

    public ViewFlow(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.inputReader = new InputReader();
        this.inputParser = new InputParser();
        this.nickname = "";
        this.running = true;
        ui = new TUI();
        this.viewState = ViewState.START;
        new Thread().start();
    }

    @Override
    public void onEventReceived(ServerEvent event) {
        System.out.println("[SERVER] Messaggio ricevuto: " + event);
        //!!questa deve gestire gli eventi dal server!!
        if (event.getEventType()== ServerEventType.UPDATE_STATE){
            call();
            ui.showShipLayout(nickname, ship);
            ui.showPlayerShipStatus(position, batteryCount, humanCount, cannonPower, enginePower, );
        }else {
            ui.showShipLayout(nickname, ship);
            ui.showPlayerShipStatus(position, batteryCount, humanCount, cannonPower, enginePower, );
            call();
        }
    }

    public void start() {
        // Thread per input utente
        new Thread(() -> {
            ClientEvent event=null;
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
                switch (viewState) {
                    case START-> { event = inputParser.parseLogin(input);}
                    case BULDING-> { event = inputParser.parseBuilding(input);}
                    case FLIGTH-> { event = inputParser.parseFlight(input);}
                }
                if (event != null) {
                    clientHandler.handleEvent(event);
                }
            }
        }, "UserInputThread").start();

    }

    public void run() {
        start();
        while (running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

