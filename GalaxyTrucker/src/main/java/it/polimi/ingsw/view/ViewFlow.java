package it.polimi.ingsw.view;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ClientHandler;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.client.ServerEventVisitor;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.text.TUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewFlow implements NetworkListenerClient.EventReceiver {
    private final ClientHandler clientHandler;
    private boolean done = false;
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
    private AvailableComponents avComponents;
    private Map<String, Integer> goodsByColor;
    private Map<String, Integer> track;
    private ServerEventHandlerVisitor serverEventVisitor;
    private Component selectedComponent;
    private volatile boolean running;
    private List<Component> keptComponent;
    private ViewState viewState;
    public enum ViewState {
        FIRST,
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
        this.avComponents = new AvailableComponents();
        ui = new TUI();
        this.viewState = ViewState.FIRST;
        this.serverEventVisitor = new ServerEventHandlerVisitor(this);
        new Thread(inputReader, "InputReaderThread").start();
    }

    @Override
    public void onEventReceived(ServerEvent event) {
        System.out.println("[SERVER] Messaggio ricevuto: " + event.getMessage() + ". || EventType ||: " + event.getEventType());
        //!!questa deve gestire gli eventi dal server!!
        if(event.getEventType()==ServerEventType.FIRST_CONNECTION){
            ui.showWelcome();
            ui.showInsertNumPlayers();
        } else {
            if (event.getEventType() == ServerEventType.UPDATE_STATE || event.getEventType() == ServerEventType.UPDATE_TRACK || event.getEventType() == ServerEventType.KEPT_COMPONENT || event.getEventType() == ServerEventType.AVAILABLE_COMPONENTS) {
                event.call(serverEventVisitor);
                done = true;
            }
            if(event.getEventType() == ServerEventType.CHOOSE_COLOURS){
                viewState = ViewState.START;
            }
            switch (viewState) {
                case START -> {
                    event.call(serverEventVisitor);
                    done = true;
                }
                case BULDING -> {
                    if (!(event.getEventType() == ServerEventType.COMPONENT_SELECTED)) {
                        ui.showAvailableComponent(avComponents);
                    }
                    ui.showShipLayout(nickname, ship);
                    ui.showPlayerShipStatus(position, batteryCount, humanCount, cannonPower, enginePower, goodsByColor);
                    ui.showKeptComponents(keptComponent);

                }
                case FLIGTH -> {
                    ui.showShipLayout(nickname, ship);
                    ui.showPlayerShipStatus(position, batteryCount, humanCount, cannonPower, enginePower, goodsByColor);
                    ui.showTrack(track);
                }
                case END -> {
                    event.call(serverEventVisitor);
                }
            }
            if (!done) {
                event.call(serverEventVisitor);
            }
            done = false;
        }
    }

    public void start() {
        // Thread per input utente
        ui.showWelcome();
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
                    case FIRST -> event = inputParser.parseFirstConnection(input);
                    case START-> { event = inputParser.parseLogin(input);}
                    case BULDING-> { event = inputParser.parseBuilding(input, selectedComponent);}
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

    public Component[][] getShip() {
        return ship;
    }

    public void setShip(Component[][] ship) {
        this.ship = ship;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getBatteryCount() {
        return batteryCount;
    }

    public void setBatteryCount(int batteryCount) {
        this.batteryCount = batteryCount;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public int getCannonPower() {
        return cannonPower;
    }

    public void setCannonPower(int cannonPower) {
        this.cannonPower = cannonPower;
    }

    public int getHumanCount() {
        return humanCount;
    }

    public void setHumanCount(int humanCount) {
        this.humanCount = humanCount;
    }

    public Map<String, Integer> getGoodsByColor() {
        return goodsByColor;
    }

    public void setGoodsByColor(Map<String, Integer> goodsByColor) {
        this.goodsByColor = goodsByColor;
    }

    public Map<String, Integer> getTrack() {
        return track;
    }

    public void setTrack(Map<String, Integer> track) {
        this.track = track;
    }

    public void setViewState(ViewState viewState) {
        this.viewState = viewState;
    }

    public void setAvComponents(AvailableComponents availableComponents) {
        this.avComponents.getComponents().addAll(availableComponents.getComponents());
    }
    public void setSelectedComponent(Component selectedComponent) {
        this.selectedComponent = selectedComponent;
    }

    public void deleteSelectedComponent() {
        this.selectedComponent = null;
    }

    public Component getSelectedComponent() {
        return selectedComponent;
    }

    public void addKeptComponent(Component keptComponent) {
        this.keptComponent.add(keptComponent);
    }

    public Component getSelectedKeptComponent() {
        return selectedComponent;
    }
}

