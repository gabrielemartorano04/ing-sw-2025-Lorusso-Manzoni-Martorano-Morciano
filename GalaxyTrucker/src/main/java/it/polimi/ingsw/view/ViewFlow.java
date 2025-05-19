package it.polimi.ingsw.view;

import it.polimi.ingsw.model.AvailableComponents;
import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.components.Cabin;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.socket.client.ClientHandler;
import it.polimi.ingsw.net.socket.client.NetworkListenerClient;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.client.ServerEventVisitor;
import it.polimi.ingsw.net.socket.events.clientevents.AlienCoordinatesRequest;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.GeneriErrorFromClient;
import it.polimi.ingsw.net.socket.events.clientevents.cardevents.OkStartFlight;
import it.polimi.ingsw.net.socket.events.serverevents.GenericErrorInsertedData;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.text.TUI;
import it.polimi.ingsw.view.text.TimerDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewFlow implements NetworkListenerClient.EventReceiver {
    private final ClientHandler clientHandler;
    private boolean done = false;
    private final InputReader inputReader;
    private final InputParser inputParser;
    private final InputParserFlight inputParserFlight;
    private final UI ui;
    private String nickname;
    private Component[][] ship;
    private int position;
    private int batteryCount;
    private int enginePower;
    private int cannonPower;
    private int humanCount;
    private Map<String, Integer> goodsByColor;
    private Map<String, Integer> track;
    private ServerEventHandlerVisitor serverEventVisitor;
    private Component selectedComponent;
    private int selectedComponentIndex; // is equal -1 if selectedComponent is null
    private ArrayList<Component> availableComponents;
    private volatile boolean running;
    private ArrayList<Component> keptComponent= new ArrayList<>();
    private ViewState viewState;
    private TimerDisplay timerDisplay;
    private ArrayList<Card>[] stacks;
    private boolean isFromKept;
    private boolean nameAlreadySet;
    private boolean end;
    private boolean sentCabinCoord;
    private boolean startFlight,startFlightForAll;


    public void setStacks(ArrayList<Card>[] stacks) {
        this.stacks=stacks;
    }

    public enum ViewState {
        FIRST,
        START,
        BULDING,
        FILL,
        FLIGTH,
        END;
    }

    public ViewFlow(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.inputReader = new InputReader();
        this.inputParser = new InputParser();
        this.inputParserFlight = new InputParserFlight();
        this.nickname = null;
        this.running = true;
        ui = new TUI();
        this.viewState = ViewState.FIRST;
        this.timerDisplay = new TimerDisplay();
        this.serverEventVisitor = new ServerEventHandlerVisitor(this);
        this.availableComponents= new ArrayList<>();
        new Thread(inputReader, "InputReaderThread").start();
        this.selectedComponentIndex = -1;
        this.isFromKept = false;
        this.nameAlreadySet = false;
        this.end = false;
        this.sentCabinCoord = false;
        this.goodsByColor = new HashMap<>(){
            {
                put("RED",0);
                put("YELLOW",0);
                put("GREEN",0);
                put("BLUE",0);
            }
        };
        this.startFlight=true;
        this.startFlightForAll=false;
    }

    @Override
    public void onEventReceived(ServerEvent event) {
        if(nickname == null) {
            System.out.println("[SERVER] Message received: " + event.getMessage() + ". || EventType ||: " + event.getEventType());

        }else {
            System.out.println("[SERVER] Message received: " + event.getMessage() + ". || EventType ||: " + event.getEventType() + " per " + nickname);
        }
        //!!questa deve gestire gli eventi dal server!!
        if(event.getEventType()==ServerEventType.FIRST_CONNECTION){
            ui.showWelcome();
            ui.showInsertNumPlayers();
        } else {
            if (event.getEventType() == ServerEventType.UPDATE_STATE || event.getEventType() == ServerEventType.UPDATE_TRACK || event.getEventType() == ServerEventType.KEPT_COMPONENT || event.getEventType() == ServerEventType.AVAILABLE_COMPONENTS || event.getEventType()==ServerEventType.COMPONENT_LINKED
            || event.getEventType()==ServerEventType.COMPONENT_SELECTED || event.getEventType()==ServerEventType.DISCART_SUCCESS ) {
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
                    ui.showShipLayout(nickname, ship);
                    if (keptComponent != null) {
                        ui.showKeptComponents(keptComponent);
                    }
                    if (getSelectedComponent() == null) {
                        ui.showBuildOptions();
                    }

                }
                case FILL->{
                    ui.showShipLayout(nickname, ship);

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
                    case FIRST -> {
                        event = inputParser.parseFirstConnection(input);
                    }
                    case START-> { event = inputParser.parseLogin(input);}
                    case BULDING-> {
                        String[] tokens = input.split("\\s+", 3);
                        String command = tokens[0].toLowerCase();
                        if (!this.end) {
                            if (command.equalsIgnoreCase("turn") && selectedComponent != null) {
                                selectedComponent.turn();
                                ui.showComponentSelected(selectedComponent);
                                event = null;
                            } else {
                                event = inputParser.parseBuilding(input, selectedComponent, selectedComponentIndex, keptComponent, isFromKept);
                            }
                        }else {
                            System.out.println("You can only wait for others to finish buildin");
                            event = null;
                        }

                    }
                    case FILL->{
                        if(!sentCabinCoord){
                            event= new AlienCoordinatesRequest();
                            sentCabinCoord=true;
                        }

                        else event= inputParser.parseFillTheShip(input);
                    }
                    case FLIGTH-> {
                        if(startFlight){
                            event=new OkStartFlight();
                            startFlight=false;
                        }
                        else if(startFlightForAll) {
                            event = inputParserFlight.parseFlight(input);
                        }
                        else {
                        System.out.println("You can only wait for others to start flight");
                        event = null;
                        }
                    }
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

    public void setEnd() {
        this.end = true;
    }
    public boolean getEnd(){
        return end;
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

    public void setSelectedComponent(int index, boolean isFromKept) {
        this.selectedComponent = availableComponents.get(index);
        this.selectedComponentIndex = index;
        this.isFromKept = isFromKept;
    }

    /**
     * serve anche cosi
     * @param component
     */
    public void setSelectedComponent(Component component, boolean isFromKept) {
        this.selectedComponent = component;
        this.isFromKept = isFromKept;
    }

    public void deleteSelectedComponent() {
        this.selectedComponent = null;
        this.selectedComponentIndex = -1;
    }

    public Component getSelectedComponent() {
        return selectedComponent;

    }

    public void addKeptComponent(Component keptComponent) throws IllegalArgumentException{
        if(this.keptComponent.size()<2) {
            this.keptComponent.add(keptComponent);
        }else throw new IllegalArgumentException("no more space to keep components");
    }

    public void removeKeptComponent(int index) {
        this.keptComponent.remove(index);
    }

    public ArrayList<Component> getSelectedKeptComponent() {
        return keptComponent;
    }

    public TimerDisplay getTimerDisplay() {
        return timerDisplay;
    }

    public void fillAvailableComponents(ArrayList<Component> availableComponents) {
        this.availableComponents.clear();
        this.availableComponents.addAll(availableComponents);
    }

    public ArrayList<Component> getAvailableComponents() {
        return availableComponents;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.nameAlreadySet = true;
    }

    public boolean getNameAlreadySet() {
        return nameAlreadySet;
    }

    public String getNickname(){
        return nickname;
    }

    public void componentAsSelected(int index){
        availableComponents.get(index).setSelected(true);
    }

    public void componentAsUnselected(int index){
        availableComponents.get(index).setSelected(false);
    }

    /**
     * sets the component at the index as not Covered since it has been selected by a client
     * @param index
     */
    public void componentAsUncovered(int index){
        availableComponents.get(index).setCovered(false);
    }


    public void setStartFlight() {
        this.startFlight = true;
    }
    public void setStartFlightForAll(){
        this.startFlight = true;
    }

    public void setParserFlightState(ParserState state) {
        inputParserFlight.setParserState(state);
    }
    public ParserState getParserFlightState() {
        return inputParserFlight.getParserState();
    }

}

