package it.polimi.ingsw.view;

import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.EventReceiver;
import it.polimi.ingsw.net.events.clientevents.PongEvent;
import it.polimi.ingsw.net.events.clientevents.cardevents.DeadTrucker;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;
import it.polimi.ingsw.net.socket.client.ClientEventSender;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.clientevents.cardevents.OkStartFlight;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.graphic.GUI;
import it.polimi.ingsw.view.text.TUI;
import it.polimi.ingsw.view.text.TimerDisplay;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewFlow implements EventReceiver {
    private final ClientEventSender sender;
    private RemoteClientHandler rmiClient;
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
    private float cannonPower;
    private int humanCount;
    private Map<String, Integer> goodsByColor;
    private Map<String, Integer> track;
    private final ServerEventHandlerVisitor serverEventVisitor;
    private Component selectedComponent;
    private int selectedComponentIndex; // is equal -1 if selectedComponent is null
    private final ArrayList<Component> availableComponents;
    private volatile boolean running;
    private final ArrayList<Component> keptComponent= new ArrayList<>();
    private ViewState viewState;
    private final TimerDisplay timerDisplay;
    private boolean isFromKept;
    private boolean nameAlreadySet;
    private boolean end;
    private boolean startFlight;
    private boolean startFlightForAll;
    private int stackSelected;
    private boolean out;
    private int level;
    private boolean alreadyLogged;


    public enum ViewState {
        FIRST,
        CHOOSE_OLD,
        START,
        BULDING,
        FILL,
        FLIGTH,
        END
    }

    public ViewFlow(ClientEventSender sender, boolean useGui) {
        this.sender = sender;
        this.inputReader = new InputReader();
        this.inputParser = new InputParser();
        this.inputParserFlight = new InputParserFlight();
        this.nickname = null;
        this.running = true;
        this.viewState = ViewState.FIRST;
        this.timerDisplay = new TimerDisplay();

        this.availableComponents= new ArrayList<>();
        new Thread(inputReader, "InputReaderThread").start();
        this.selectedComponentIndex = -1;
        this.isFromKept = false;
        this.nameAlreadySet = false;
        this.end = false;
        this.stackSelected=-1;
        this.ship = new Component[11][12];
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
        this.selectedComponent=null;
        this.out=false;
        this.level=0;
        if(useGui){
            this.ui=new GUI(this);
        }else{
            this.ui = new TUI(this);
        }
        this.serverEventVisitor = new ServerEventHandlerVisitor(this, ui);
        this.alreadyLogged=false;
    }

    public ViewState getViewState() {
        return viewState;
    }


    @Override
    public void onEventReceived(ServerEvent event) {
        if (event.getEventType() != ServerEventType.PING) {
//            if (nickname == null) {
//                System.out.println("[SERVER] Message received: " + event.getMessage() + ". || EventType ||: " + event.getEventType());
//
//            } else {
//                System.out.println("[SERVER] Message received: " + event.getMessage() + ". || EventType ||: " + event.getEventType() + " per " + nickname);
//            }
        //!!questa deve gestire gli eventi dal server!!
            switch (event.getEventType()) {
                case CHOOSE_RESUME ->{
                    System.out.println("CHOOSE_RESUME");
                    viewState = ViewState.CHOOSE_OLD;
                }
                case FIRST_CONNECTION-> {
                    ui.showInsertNumPlayers();
                }
                default -> {
                    if (event.getEventType() == ServerEventType.UPDATE_STATE || event.getEventType() == ServerEventType.UPDATE_TRACK || event.getEventType() == ServerEventType.KEPT_COMPONENT || event.getEventType() == ServerEventType.AVAILABLE_COMPONENTS || event.getEventType() == ServerEventType.COMPONENT_LINKED
                            || event.getEventType() == ServerEventType.COMPONENT_SELECTED || event.getEventType() == ServerEventType.DISCART_SUCCESS || event.getEventType() == ServerEventType.CARD_FINISHED) {
                        event.call(serverEventVisitor);
                        done = true;
                    }
                    if (event.getEventType() == ServerEventType.CHOOSE_COLOURS) {
                        viewState = ViewState.START;
                    }
                    switch (viewState) {
                        case START -> {
                            event.call(serverEventVisitor);
                            done = true;
                        }
                        case BULDING -> {
//                        ui.showShipLayout(nickname, ship);
//                        if (!keptComponent.isEmpty()) {
//                            ui.showKeptComponents(keptComponent);
//                        }
//                        if (getSelectedComponent() == null) {
//                            ui.showBuildOptions();
//                        }

                        }
                        case FILL -> {
                            //ui.showShipLayout(nickname, ship);
                        }
                        case FLIGTH -> {
//                            ui.showShipLayout(nickname, ship);
//                            ui.showPlayerShipStatus(position, batteryCount, humanCount, cannonPower, enginePower, goodsByColor);
//                            ui.showTrack(track, level);
                            //System.out.println("you are out?" + out);
                        }
                        case END -> event.call(serverEventVisitor);
                    }
                    if (!done) {
                        event.call(serverEventVisitor);
                    }
                    done = false;
                }
            }
        }
    }

    /**
     * this method is used by the TUI to read client's input while the GUI directly calls handleInput
     */
    public void start() {
        // Thread per input utente
        new Thread(() -> {
            while (running) {
                String input;
                try {

                    input = inputReader.nextLineBlocking();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (input == null || input.equalsIgnoreCase("exit")) {
                    running = false;
                    break;
                }
                handleInput(input);
            }
        }, "UserInputThread").start();

    }

    /**
     * method called by viewFlow and GUI's handlers to generate and send events to the server
     */
    public void handleInput(String input){
        //TODO: gestire il caso in cui input è exit per la GUI
        ClientEvent event=null;
        switch (viewState) {
            case CHOOSE_OLD -> event=inputParser.parseOld(input);
            case FIRST -> event = inputParser.parseFirstConnection(input);
            case START-> {
                if(!alreadyLogged)
                    event = inputParser.parseLogin(input);
                else {
                    event = null;
                }
            }
            case BULDING-> {
                if (!this.end) {
                    event = inputParser.parseBuilding(input, this.selectedComponent, selectedComponentIndex, keptComponent, isFromKept, stackSelected);
                }
                else {
                    ui.showWaitForOthers();
                    event = null;
                }

            }
            case FILL-> event= inputParser.parseFillTheShip(input);
            case FLIGTH-> {
                if(!out) {
                    if (startFlight) {
                        if(input.trim().equals("state")){
                            event = new OkStartFlight();
                            startFlight = false;
                        }
                        else{
                            System.out.println("Error: Invalid command. Type 'state'.");
                            event=null;
                        }
                    } else if (startFlightForAll) {
                        event = inputParserFlight.parseFlight(input, nickname);
                    } else {
                        ui.showWaitForOthers();
                        event = null;
                    }
                }else{
                    event = new DeadTrucker();
                    System.out.println("You are out of the flight, please wait for others to finish it and see the results");
                }
            }
        }
        if (event != null) {
            handleOutgoingEvent(event);
        }
    }

//    public void run() {
//        start();
//        while (running) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }

    public void setRmiClient(RemoteClientHandler rmiClient) {
        this.rmiClient = rmiClient;
    }

    public void handleOutgoingEvent(ClientEvent event) {
        if (this.sender != null){
            sender.handleEvent(event);
        }else if(this.rmiClient != null){
            try {
                rmiClient.handleOutgoingEvent(event);
            } catch (RemoteException e) {
                System.out.println("RemoteException: " + e);
            }
        }
    }

    public Component[][] getShip() {
        return ship;
    }

    public void setShip(Component[][] ship) {
        this.ship=ship;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

//    public int getBatteryCount() {
//        return batteryCount;
//    }

    public void setBatteryCount(int batteryCount) {
        this.batteryCount = batteryCount;
    }

//    public int getEnginePower() {
//        return enginePower;
//    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

//    public int getCannonPower() {
//        return cannonPower;
//    }

    public void setCannonPower(float cannonPower) {
        this.cannonPower = cannonPower;
    }

//    public int getHumanCount() {
//        return humanCount;
//    }

    public void setHumanCount(int humanCount) {
        this.humanCount = humanCount;
    }

//    public Map<String, Integer> getGoodsByColor() {
//        return goodsByColor;
//    }

    public void setGoodsByColor(Map<String, Integer> goodsByColor) {
        this.goodsByColor = goodsByColor;
    }

    public void setEnd() {
        this.end = true;
    }

//    public boolean getEnd(){
//        return end;
//    }

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
     */
    public void setSelectedComponent(Component component, boolean isFromKept) {
        this.selectedComponent = component;
        this.isFromKept = isFromKept;
    }
    public void setSelectedComponent(Component component) {
        this.selectedComponent = component;
    }

    public void deleteSelectedComponent() {
        this.selectedComponent = null;
        this.selectedComponentIndex = -1;
    }

    public Component getSelectedComponent() {
        return selectedComponent;

    }

    public Map<String, Integer> getGoodsByColor() {
        return goodsByColor;
    }

    public void addKeptComponent(Component keptComponent) throws IllegalArgumentException{
        if(this.keptComponent.size()<2) {
            this.keptComponent.add(keptComponent);
        }else throw new IllegalArgumentException("no more space to keep components");
    }

//    public void removeKeptComponent(int index) {
//        this.keptComponent.remove(index);
//    }

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
     */
    public void componentAsUncovered(int index){
        availableComponents.get(index).setCovered(false);
    }


    public void setStartFlight() {
        this.startFlight = true;
    }
    public void setStartFlightForAll(){
        this.startFlightForAll = true;
    }

    public void setParserFlightState(ParserState state) {
        inputParserFlight.setParserState(state);
    }
    public ParserState getParserFlightState() {
        return inputParserFlight.getParserState();
    }

    public void setHasStack(int i) {
        this.stackSelected = i;
    }

    public void setOut() {
        this.out = true;
    }
    public boolean getOut() {
        return out;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }

    public int getBatteryCount(){
        return batteryCount;
    }
    public int getEnginePower(){
        return enginePower;
    }
    public float getCannonPower(){
        return cannonPower;
    }
    public int getHumanCount(){
        return humanCount;
    }

    public void respondToPing() {
            if (this.sender != null){
                sender.handleEvent(new PongEvent());
            }else if(this.rmiClient != null){
                try {
                    rmiClient.handleOutgoingEvent(new PongEvent());
                } catch (RemoteException e) {
                    System.out.println("RemoteException: " + e);
                }
            }
    }

    public GUI getGUI(){
        return (GUI)ui;
    }

    public void setAlreadyLogged(){
        this.alreadyLogged = true;
    }
}

