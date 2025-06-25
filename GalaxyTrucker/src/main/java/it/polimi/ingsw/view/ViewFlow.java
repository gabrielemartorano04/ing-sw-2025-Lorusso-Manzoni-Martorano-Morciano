package it.polimi.ingsw.view;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Load;
import it.polimi.ingsw.model.components.Shield;
import it.polimi.ingsw.net.EventReceiver;
import it.polimi.ingsw.net.events.clientevents.PongEvent;
import it.polimi.ingsw.net.events.clientevents.cardevents.DeadTrucker;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;
import it.polimi.ingsw.net.socket.client.ClientEventSender;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.clientevents.cardevents.OkStartFlight;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.graphic.GUI;
import it.polimi.ingsw.view.text.TUI;
import it.polimi.ingsw.view.text.TimerDisplay;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewFlow implements EventReceiver {

    /**
     * Socket Sender if the client has chosen SOCKET
     */
    private final ClientEventSender sender;

    /**
     * Rmi client if the client has chosen RMI
     */
    private RemoteClientHandler rmiClient;

    /**
     * Reader of client's input if he chose TUI
     */
    private final InputReader inputReader;

    /**
     * Input parser of input coming from both GUI and TUI, used for initial phase and building phase
     */
    private final InputParser inputParser;

    /**
     * Input parser of input coming from both GUI and TUI, used for the flight phase
     */
    private final InputParserFlight inputParserFlight;

    /**
     * User interface that represents both GUI and TUI
     */
    private final UI ui;

    /**
     * Visitor that handles all the incoming events from the Server to this client
     */
    private final ServerEventHandlerVisitor serverEventVisitor;

    /**
     * Represents the running status of the view
     */
    private volatile boolean running;

    /**
     * States if this client is already logged
     */
    private boolean alreadyLogged;

    /**
     * States if the nickname of the current class has already been setted
     */
    private boolean nameAlreadySet;

    /**
     * Nickname associated to this view chosen by the client
     */
    private String nickname;

    /**
     * States in which the view can be
     */
    public enum ViewState {
        FIRST,
        CHOOSE_OLD,
        START,
        BULDING,
        FILL,
        FLIGTH,
        END
    }

    /**
     * Current viewState
     */
    private ViewState viewState;

    /**
     * Boolean value that is true if the building phase has ended
     */
    private boolean endBuildingPhase;

    /**
     * Boolean value that is true if this client is ready for the flight phase
     */
    private boolean startFlight;

    /**
     * Boolean value that is true if all clients are in the flight phase and therefore the flight phase can start
     */
    private boolean startFlightForAll;

    /**
     * Timer associated to the hourglass
     */
    private final TimerDisplay timerDisplay;

    /**
     * States if the client has been eliminated
     */
    private boolean out;

    /**
     * Level of the game the client is in
     */
    private int level;

    /**
     * Number of flight days this player has flown
     */
    private int position;

    /**
     * Track positions of all players in the game updated to the last change
     */
    private Map<String, Integer> track;

    /**
     * Current number of batteries the client has to remove if he has to
     */
    private int numOfBatteriesToRemove;

    /**
     * Number of cards to draw in the flight scene
     */
    private int cardsToGo;

    /**
     * True if the client has linked at least one component to his/her ship
     */
    private boolean firstLinked;

    /**
     * Current number of cosmic credits the client has
     */
    private int credits;

    /**
     * True if the client has currently selected a stack
     */
    private int stackSelected;

    /**
     * List of the available components for the building phase always updated for all clients
     */
    private final ArrayList<Component> availableComponents;

    /**
     * Current component selected by this client, null if he/she hasn't selected a component
     */
    private Component selectedComponent;

    /**
     * Index of the selected Component within the available Components list, equals to -1 if selectedComponent is null
     */
    private int selectedComponentIndex;

    /**
     * List of kept components of this client
     */
    private final ArrayList<Component> keptComponent= new ArrayList<>();

    /**
     * True if selectedComponent comes from the keptComponents list, false if it comes from the available Components
     */
    private boolean isFromKept;

    /**
     * Ship of this client always updated
     */
    private SpaceShip ship;

    private Map<String, Integer> goodsByColor;



    public ViewFlow(ClientEventSender sender, boolean useGui) {
        this.sender = sender;
        this.inputReader = new InputReader();
        this.nickname = null;
        this.running = true;
        this.viewState = ViewState.FIRST;
        this.credits=0;
        this.numOfBatteriesToRemove = 0;

        this.availableComponents= new ArrayList<>();
        new Thread(inputReader, "InputReaderThread").start();
        this.selectedComponentIndex = -1;
        this.isFromKept = false;
        this.nameAlreadySet = false;
        this.endBuildingPhase = false;
        this.stackSelected=-1;
        this.ship = new SpaceShip();
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
        this.inputParser = new InputParser(ui);
        this.inputParserFlight = new InputParserFlight(ui);


        this.timerDisplay = new TimerDisplay(ui);
        this.serverEventVisitor = new ServerEventHandlerVisitor(this, ui);
        this.alreadyLogged=false;
        this.firstLinked=false;
    }

    public void setFirstLinked(){
        this.firstLinked=true;
    }

    public boolean getFirstLinked(){
        return this.firstLinked;
    }

    public ViewState getViewState() {
        return viewState;
    }


    @Override
    public void onEventReceived(ServerEvent event) {
        event.call(serverEventVisitor);
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
                if (!this.endBuildingPhase) {
                    event = inputParser.parseBuilding(input, this.selectedComponent, selectedComponentIndex, keptComponent, isFromKept, stackSelected,firstLinked);
                }
                else {
                    ui.showWaitForOthers();
                    event = null;
                }

            }
            case FILL-> event= inputParser.parseFillTheShip(input);
            case FLIGTH-> {
                if(!out) {
                    if (this.startFlight) {
                        if(input.trim().equals("state")){
                            event = new OkStartFlight();
                            this.startFlight = false;
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

    public SpaceShip getShip() {
        return ship;
    }

    public void setShip(SpaceShip ship) {
        this.ship=ship;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setEnd() {
        this.endBuildingPhase = true;
    }

    public void setNumOfBatteriesToRemove(int numOfBatteriesToRemove) {
        this.numOfBatteriesToRemove = numOfBatteriesToRemove;
    }

    public int getNumOfBatteriesToRemove() {
        return numOfBatteriesToRemove;
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
        this.goodsByColor = ship.getGoodsByColor();
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

    public ArrayList<Component> getKeptComponent() {
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


//    public void setStartFlight() {
//        this.startFlight = true;
//    }
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

    public boolean isOut() {
        return out;
    }

    public void setLevel(int level) {
        this.level = level;
        this.inputParser.setLevel(level);
        if(level == 2) {
            this.cardsToGo = 12;
        }else this.cardsToGo = 9;
    }
    public int getLevel() {
        return level;
    }

    public int getBatteryCount(){
        return ship.getNumBatteries();
    }
    public int getEnginePower(){
        return ship.getTotalEnginePower();
    }
    public float getCannonPower(){
        return ship.getTotalCannonPower();
    }
    public int getHumanCount(){
        return ship.getNumCrew();
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

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getCredits() {
        return credits;
    }

    public void decreaseCardsToGo() {
        this.cardsToGo--;
    }

    public int getCardsToGo() {
        return cardsToGo;
    }

    public void setStartFlight(boolean startFlight) {
        this.startFlight = startFlight;
    }

    public int getLostComponents() {
        return ship.getLost();
    }

    public boolean shipHasDoubleEngines(){
        if(ship.getNumDoubleEngine()>0 && ship.getNumBatteries() > 0){
            return true;
        }else{
            this.handleInput("n");
            return false;
        }
    }

    public boolean shipHasDoubleCannons(){
        if(ship.getNumDoubleCannon()>0 && ship.getNumBatteries() > 0){
            return true;
        }else{
            this.handleInput("n");
            return false;
        }
    }

    public boolean shipHasShields(){
        if(ship.getNumBatteries() == 0){
            this.handleInput("n");
            return false;
        }else{
            Component[][] components = ship.getComponents();
            for (int i = 5; i <= 9; i++) {
                for (int j = 4; j <= 10; j++) {
                    if (components[i][j] != null) {
                        switch (components[i][j]) {
                            case Shield s -> {
                                return true;
                            }
                            default -> {}
                        }
                    }
                }
            }
            this.handleInput("n");
            return false;
        }
    }

    public boolean shipHasLoad(){
        Component[][] components = ship.getComponents();
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch (components[i][j]) {
                        case Load l -> {
                            return true;
                        }
                        default -> {}
                    }
                }
            }
        }
        this.handleInput("n");
        return false;
    }

    public boolean shipHasGoods(){
        if(ship.getGoodsList().isEmpty()){
            this.handleInput("n");
            return false;
        }
        return true;
    }
}

