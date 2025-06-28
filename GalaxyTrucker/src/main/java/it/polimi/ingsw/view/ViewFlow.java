package it.polimi.ingsw.view;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Load;
import it.polimi.ingsw.model.components.Shield;
import it.polimi.ingsw.net.events.clientevents.PongEvent;
import it.polimi.ingsw.net.events.clientevents.cardevents.DeadTrucker;
import it.polimi.ingsw.net.rmi.client.RemoteClientHandler;
import it.polimi.ingsw.net.socket.client.ClientSocketSender;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
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

/**
 * The ViewFlow class is responsible for managing the flow of logic and events in the client-side
 * application. It serves as a bridge between the GUI/TUI views and the server, handling user input,
 * managing game states, and sending or receiving events
 */
public class ViewFlow implements EventReceiver {

    /**
     * Socket Sender if the client has chosen SOCKET
     */
    private final ClientSocketSender sender;

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
     * All players nickname
     */
    private ArrayList<String> players;

    /**
     *
     */
    private boolean stateNotSended;

    /**
     * States in which the view can be
     */
    public enum ViewState {
        FIRST,
        CHOOSE_OLD,
        START,
        BULDING,
        REMOVE,
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
     * Boolean value that is true if all clients are in the flight phase and therefore the flight phase can start
     */
    private boolean startFlightForAll;

    /**
     * Timer associated with the hourglass
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
     * The current number of batteries the client has to remove if he has to
     */
    private int numOfBatteriesToRemove;

    /**
     * Current number of cosmic credits the client can gain
     */
    private int cosmicCreditsToGain;

    /**
     * Current number of days to lose
     */
    private int daysToLose;

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
     * Index of the selected Component within the available Components list equals to -1 if selectedComponent is null
     */
    private int selectedComponentIndex;

    /**
     * List of client's kept components
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

    /**
     * Map associating the color of the good and the number og that good in the spaceship
     */
    private Map<String, Integer> goodsByColor;

    /**
     * Constructs an instance of the ViewFlow class and initializes its state, components, and dependencies
     * @param sender the ClientEventSender used to send events from the view to the server
     * @param useGui a boolean indicating whether to use the graphical user interface (GUI) or the textual user interface (TUI)
     */
    public ViewFlow(ClientSocketSender sender, boolean useGui) {
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
        this.startFlightForAll=false;
        this.selectedComponent=null;
        this.stateNotSended=true;

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

    /**
     * Sets the state of the 'firstLinked' field to true.
     * This method is used to mark the first link in the process or sequence
     * within the context of the current ViewFlow instance.
     */
    public void setFirstLinked(){
        this.firstLinked=true;
    }

    /**
     * This method sets all players nicknames to display during building phase
     * @param players all players nicknames
     */
    public void setPlayers(ArrayList<String> players) {
        this.players=players;
    }

    /**
     * Retrieves the current state of the view
     * @return the current ViewState of the view, representing the phase or state
     *         in which the view is operating.
     */
    public ViewState getViewState() {
        return viewState;
    }

    /**
     * Handles the reception of a server event and delegates its processing to the appropriate handler
     * @param event the server event received, containing the data and behavior to handle
     */
    @Override
    public void onEventReceived(ServerEvent event) {
        //TODO: da togliere sta print
        System.out.println(event.getMessage() + " from: " + event.getClass().getName());
        event.call(serverEventVisitor);
    }

    /**
     * This method is used by the TUI to read client's input while the GUI directly calls handleInput
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
     * Method called by viewFlow and GUI's handlers to generate and send events to the server
     */
    public void handleInput(String input){

        System.out.println(this.getViewState());

        ClientEvent event=null;
        switch (viewState) {
            case CHOOSE_OLD -> event=inputParser.parseOld(input);
            case FIRST -> event = inputParser.parseFirstConnection(input);
            case START-> {
                if(!alreadyLogged) {
                    event = inputParser.parseLogin(input);
                }
            }
            case BULDING-> {
                if (!this.endBuildingPhase) {
                    event = inputParser.parseBuilding(input, this.selectedComponent, selectedComponentIndex, keptComponent, isFromKept, stackSelected,firstLinked, players);
                }
            }
            case REMOVE -> event = inputParser.parseRemove(input);
            case FILL-> event= inputParser.parseFillTheShip(input);
            case FLIGTH-> {
                if(!out) {
                    if(stateNotSended){
                        if(input.trim().equals("state")){
                            event = new OkStartFlight();
                        }
                        stateNotSended = false;
                    }
                    else if (startFlightForAll) {
                        event = inputParserFlight.parseFlight(input, nickname);
                    }
                }else{
                    event = inputParserFlight.parseFlight(input, nickname);
                }
            }
        }
        if (event != null) {
            handleOutgoingEvent(event);
        }
    }

    /**
     * Sets the RemoteClientHandler instance for the current ViewFlow object.
     * This method is used to define the RMI client handler responsible for communicating
     * with the remote server
     * @param rmiClient the RemoteClientHandler instance to be assigned.
     */
    public void setRmiClient(RemoteClientHandler rmiClient) {
        this.rmiClient = rmiClient;
    }

    /**
     * Handles an outgoing client event by delegating its processing to the appropriate handler
     * @param event the client event to be processed and sent. It contains the behavior and data to be
     *              handled by either a local sender or a remote client handler.
     */
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

    /**
     * @return the SpaceShip instance associated with the current view.
     */
    public SpaceShip getShip() {
        return ship;
    }

    /**
     * Sets the SpaceShip instance for the current ViewFlow object
     * @param ship the SpaceShip instance to be associated with the current view.
     */
    public void setShip(SpaceShip ship) {
        this.ship=ship;
    }

    /**
     * @return the current position as an integer, representing the state or value
     *         associated with the position field.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position value for the current instance
     * @param position the new value to be assigned to the position field.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Marks the end of the building phase in the current ViewFlow instance
     */
    public void setEndBuildingPhase() {
        this.endBuildingPhase = true;
    }

    /**
     * Sets the number of batteries to be removed
     * @param numOfBatteriesToRemove the number of batteries to be removed. Must be a non-negative integer.
     */
    public void setNumOfBatteriesToRemove(int numOfBatteriesToRemove) {
        this.numOfBatteriesToRemove = numOfBatteriesToRemove;
    }

    /**
     * @return the number of batteries to remove as an integer.
     */
    public int getNumOfBatteriesToRemove() {
        return numOfBatteriesToRemove;
    }

    /**
     * Sets the number of cosmic credits to gain
     * @param cosmicCreditsToGain the number of cosmic credits to be gained
     */
    public void setCosmicCreditsToGain(int cosmicCreditsToGain) {
        this.cosmicCreditsToGain = cosmicCreditsToGain;
    }

    /**
     * @return the number of cosmic credits to be gained as an integer.
     */
    public int getCosmicCreditsToGain() {
        return cosmicCreditsToGain;
    }

    /**
     * Sets the number of days lost
     * @param daysLost the number of days to set as lost
     */
    public void setDaysLost(int daysLost) {
        this.daysToLose = daysLost;
    }

    /**
     * @return the number of days lost as an integer
     */
    public int getDaysLost() {
        return this.daysToLose;
    }

    /**
     * @return a map that represents the track field. The keys in the
     *         map are nicknames, and the values are positions.
     */
    public Map<String, Integer> getTrack() {
        return track;
    }

    /**
     * Assigns a map to the track field of the current instance
     * @param track a map containing keys as strings and values as integers.
     *              Represents labels with their corresponding counts or values.
     */
    public void setTrack(Map<String, Integer> track) {
        this.track = track;
    }

    /**
     * Sets the state of the view
     * @param viewState the new ViewState to be assigned to the view. It represents
     *                  the current phase or state of the view's operation.
     */
    public void setViewState(ViewState viewState) {
        this.viewState = viewState;
    }

    /**
     * Sets the selected component and its index based on the given parameters
     * @param index the index of the component to be selected, referring to its position
     *              in the list of available components.
     * @param isFromKept a boolean indicating whether the selected component originates
     *                   from the kept component list.
     */
    public void setSelectedComponent(int index, boolean isFromKept) {
        this.selectedComponent = availableComponents.get(index);
        this.selectedComponentIndex = index;
        this.isFromKept = isFromKept;
    }

    /**
     * Sets the selected component for the current view along with its origin status
     * @param component the Component to be marked as selected.
     * @param isFromKept a boolean indicating whether the selected component
     *                   originates from the kept components list.
     */
    public void setSelectedComponent(Component component, boolean isFromKept) {
        this.selectedComponent = component;
        this.isFromKept = isFromKept;
    }

    /**
     * Sets the selected component for the current instance
     * @param component the Component to be assigned as the selected component.
     */
    public void setSelectedComponent(Component component) {
        this.selectedComponent = component;
    }

    /**
     * Clears the selected component and its index within the current view
     */
    public void deleteSelectedComponent() {
        this.selectedComponent = null;
        this.selectedComponentIndex = -1;
    }

    /**
     * @return the selected Component, or null if no component is currently selected.
     */
    public Component getSelectedComponent() {
        return selectedComponent;

    }

    /**
     * @return a Map where the key is a String representing the color of the goods,
     *         and the value is an Integer representing the count of goods for that color.
     */
    public Map<String, Integer> getGoodsByColor() {
        this.goodsByColor = ship.getGoodsByColor();
        return goodsByColor;
    }

    /**
     * Adds a component to the kept components list if space is available.
     * The list can hold a maximum of two components
     * @param keptComponent the Component to be added to the kept components list.
     * @throws IllegalArgumentException if the kept components list already contains two components.
     */
    public void addKeptComponent(Component keptComponent) throws IllegalArgumentException{
        if(this.keptComponent.size()<2) {
            this.keptComponent.add(keptComponent);
        }else throw new IllegalArgumentException("no more space to keep components");
    }

    /**
     * @return an ArrayList of Component objects representing the components
     *         currently stored in the kept components list.
     */
    public ArrayList<Component> getKeptComponent() {
        return keptComponent;
    }

    /**
     * @return the TimerDisplay object, which manages and displays the timer functionality
     *         within the context of the ViewFlow instance.
     */
    public TimerDisplay getTimerDisplay() {
        return timerDisplay;
    }

    /**
     * Updates the list of available components for the current instance.
     * Clears the existing list of available components and adds all components from the provided list
     * @param availableComponents the list of components to be added to the available components
     *                            of the current instance. It must be a non-null ArrayList
     *                            containing Component objects.
     */
    public void fillAvailableComponents(ArrayList<Component> availableComponents) {
        this.availableComponents.clear();
        this.availableComponents.addAll(availableComponents);
    }

    /**
     * @return an ArrayList of Component objects representing the components that are currently available.
     */
    public ArrayList<Component> getAvailableComponents() {
        return availableComponents;
    }

    /**
     * Sets the nickname for the current instance.
     * This method updates the nickname field and marks the name as already set
     * @param nickname the new nickname to be assigned. It must be a non-null, non-empty string
     *                 representing the desired nickname for the current instance.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.nameAlreadySet = true;
    }

    /**
     * @return a boolean value where true indicates that the name is already set,
     *         and false indicates that it is not set.
     */
    public boolean getNameAlreadySet() {
        return nameAlreadySet;
    }

    /**
     * @return a String representing the nickname. If no nickname has been set,
     *         the returned value may be null or an empty string depending on the implementation.
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Marks the component at the specified index as selected
     * @param index the index of the component to be selected
     */
    public void componentAsSelected(int index){
        availableComponents.get(index).setSelected(true);
    }

    /**
     * Marks the component at the specified index as unselected
     * @param index the index of the component to be marked as unselected
     */
    public void componentAsUnselected(int index){
        availableComponents.get(index).setSelected(false);
    }

    /**
     * Marks a specific component, identified by its index, as uncovered
     * @param index the index of the component in the list of available components to be marked as uncovered
     */
    public void componentAsUncovered(int index){
        availableComponents.get(index).setCovered(false);
    }


    /**
     * Sets the 'startFlightForAll' flag to true, indicating that all flights
     * are ready to start
     */
    public void setStartFlightForAll(){
        this.startFlightForAll = true;
    }

    /**
     * Updates the parser state for the inputParserFlight instance
     * @param state the new state to set for the parser
     */
    public void setParserFlightState(ParserState state) {
        inputParserFlight.setParserState(state);
    }

    /**
     * @return the current state of the flight input parser as a ParserState object
     */
    public ParserState getParserFlightState() {
        return inputParserFlight.getParserState();
    }

    /**
     * Sets the stackSelected value to indicate whether a stack is selected
     * @param i the integer value to set the stackSelected attribute
     */
    public void setHasStack(int i) {
        this.stackSelected = i;
    }

    /**
     * Sets the output state to true
     */
    public void setOut() {
        this.out = true;
    }

    /**
     * @return true if the state is "out", false otherwise
     */
    public boolean isOut() {
        return out;
    }

    /**
     * Sets the level of the game and adjusts the number of cards required
     * accordingly. It also updates the input parser with the specified level
     * @param level the game level to be set; typically determines difficulty
     *              and the number of cards needed
     */
    public void setLevel(int level) {
        this.level = level;
        this.inputParser.setLevel(level);
        if(level == 2) {
            this.cardsToGo = 12;
        }else this.cardsToGo = 8;
    }

    /**
     * @return the current level as an integer.
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the number of batteries in the ship.
     */
    public int getBatteryCount(){
        return ship.getNumBatteries();
    }

    /**
     * @return the total engine power as an integer value
     */
    public int getEnginePower(){
        return ship.getTotalEnginePower();
    }

    /**
     * @return the total cannon power as a float value
     */
    public float getCannonPower(){
        return ship.getTotalCannonPower();
    }

    /**
     * @return the total number of human crew members aboard the ship.
     */
    public int getHumanCount(){
        return ship.getNumCrew();
    }

    /**
     * Responds to a ping event by sending a corresponding PongEvent
     */
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

    /**
     * @return the GUI instance cast from the 'ui' object
     */
    public GUI getGUI(){
        return (GUI)ui;
    }

    /**
     * Updates the state to indicate that the action, event, or condition associated
     * with this method has already been logged
     */
    public void setAlreadyLogged(){
        this.alreadyLogged = true;
    }

    /**
     * Sets the number of credits.
     * @param credits the number of credits to be set
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * @return the number of credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Decreases the number of cards remaining to go by one
     */
    public void decreaseCardsToGo() {
        this.cardsToGo--;
    }

    /**
     * @return the number of cards still to go as an integer
     */
    public int getCardsToGo() {
        return cardsToGo;
    }


    /**
     * @return the number of lost components as an integer
     */
    public int getLostComponents() {
        return ship.getLost();
    }

    /**
     * @return true if the ship has at least one double engine and one battery, false otherwise
     */
    public boolean shipHasDoubleEngines(){
        if(ship.getNumDoubleEngine()>0 && ship.getNumBatteries() > 0){
            return true;
        }else{
            this.handleInput("n");
            return false;
        }
    }

    /**
     * @return true if the ship has at least one double cannon and one battery; false otherwise
     */
    public boolean shipHasDoubleCannons(){
        if(ship.getNumDoubleCannon()>0 && ship.getNumBatteries() > 0){
            return true;
        }else{
            this.handleInput("n");
            return false;
        }
    }

    /**
     * @return true if the ship has at least one shield; false otherwise
     */
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
                            case Shield ignored -> {
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

    /**
     * @return true if a load is present in the specified component range, false otherwise.
     */
    public boolean shipHasLoad(){
        Component[][] components = ship.getComponents();
        for (int i = 5; i <= 9; i++) {
            for (int j = 4; j <= 10; j++) {
                if (components[i][j] != null) {
                    switch (components[i][j]) {
                        case Load ignored -> {
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

    /**
     * @return true if the ship has goods, false otherwise
     */
    public boolean shipHasGoods(){
        if(ship.getGoodsList().isEmpty()){
            this.handleInput("n");
            return false;
        }
        return true;
    }
}

