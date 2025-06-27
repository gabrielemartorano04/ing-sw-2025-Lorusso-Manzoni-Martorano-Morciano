package it.polimi.ingsw.view.text;

import it.polimi.ingsw.model.PlayerColors;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Stack;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.cards.Effect;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.events.serverevents.AlienCabinCoord;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.FlightStartEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.AvailableColorsEvent;
import it.polimi.ingsw.net.events.serverevents.setupevents.ColorAlreadyInError;
import it.polimi.ingsw.net.events.serverevents.setupevents.NicknameAlreadyInError;
import it.polimi.ingsw.view.ParserState;
import it.polimi.ingsw.view.UI;
import it.polimi.ingsw.view.ViewFlow;

import java.util.*;
import java.util.List;

/**
 * The TUI class represents a text-based user interface for interacting with a game system.
 * It provides methods to handle various events and display relevant information and states to players
 */
public class TUI implements UI {

    /**
     * Represents the flow of views within the game. This variable manages the interactions
     * between the user interface and the underlying game logic, facilitating the display
     * and update of various game states and events to the user
     */
    private final ViewFlow viewFlow;

    /**
     * Constructs a new TUI (Text User Interface) object.
     * @param viewFlow the ViewFlow instance to manage the flow of scenes and events within the game.
     */
    public TUI(ViewFlow viewFlow) {
        this.viewFlow = viewFlow;
    }

    /**
     * Displays the welcome scene to the player, including a welcome message
     * and prompts the user to select a color from the available options.
     * @param event the event containing the set of available player colors
     *              during the color selection process
     */
    @Override
    public void welcomeScene(AvailableColorsEvent event){
        this.showWelcome();
        this.showChooseColor(event.getColors());

    }

    /**
     * Notifies the user that the selected nickname is already in use and prompts them
     * to choose a new nickname and select a color from the available options.
     * @param event the event containing details about the nickname conflict, including
     *              the set of available colors that the user can choose from.
     */
    @Override
    public void nickNameAlreadyTaken(NicknameAlreadyInError event){
        this.showNicknameTaken();
        this.showChooseColor(event.getColors());
    }

    /**
     * Handles the event triggered when a player already in use selects a color
     * @param event the event containing details about the error, including the already
     *              selected color and the set of available colors.
     */
    @Override
    public void colorAlreadyTaken(ColorAlreadyInError event){
        this.showError(event.getMessage());
        this.showColorTaken();
        this.showChooseColor(event.getAvailableColors());
    }

    /**
     * Notifies the player that they have successfully joined the game.
     * @param nickname the nickname of the player who has joined the game
     */
    @Override
    public void showPlayerJoined(String nickname) {
        System.out.println("Congratulation " + nickname + "! You've joined the game!.");
    }

    /**
     * Resumes the building phase of the scene and updates the interface with the current game state such as available components,
     * the ship layout, kept components, and the selected component.
     * @param playerNames a list containing the nicknames of players currently involved in the scene
     */
    @Override
    public void resumeBuildingScene(ArrayList<String> playerNames){
        this.showAvailableComponent(viewFlow.getAvailableComponents());
        this.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        this.showKeptComponents(viewFlow.getKeptComponent());
        this.showComponentSelected(viewFlow.getSelectedComponent());
    }

    /**
     * Resumes the flight scene, updating the game state and interface to reflect
     * the ongoing activities of the players currently in the scene.
     * @param playerNames a list containing the nicknames of players currently involved in the flight scene
     */
    @Override
    public void resumeFlightScene(ArrayList<String> playerNames){

    }

    /**
     * Initiates the building phase of the game by displaying information about the players,
     * available components, and build options
     * @param event the event containing information for starting the building phase,
     *              including the list of players, available components, and other related details
     */
    @Override
    public void startBuildingScene(StartBuildingEvent event){
        this.showPlayers(event.getPlayers());
        viewFlow.setPlayers(event.getPlayers());
        this.showBuildPhaseStart(event.getComponents());
        this.showBuildOptions();
        System.out.print("\n>");
    }

    /**
     * Displays the list of available components in a formatted manner.
     * Components that are already selected or covered will be visually represented differently
     * @param components a list of Component objects representing the components to be displayed.
     *                    Only the components that are not selected will be shown.
     */
    @Override
    public void showAvailableComponent(ArrayList<Component> components) {
        System.out.println("========================================================= AVAILABLE COMPONENTS =========================================================");

        int numCols = 6;
        int colCount = 0;

        List<StringBuilder> lines = new ArrayList<>();
        for (int i = 0; i < 7; i++) lines.add(new StringBuilder()); // ogni componente ha 7 righe di output

        int i = 0;

        for (int originalIndex = 0; originalIndex < components.size(); originalIndex++) {
            Component c = components.get(originalIndex);

            if (!c.getSelected()) {
                String id = String.format("%3d", i);

                if (c.getCovered()) {
                    lines.get(0).append(id).append(") _______            ");
                    lines.get(1).append("    |       |           ");
                    lines.get(2).append("    |       |           ");
                    lines.get(3).append("    |       |           ");
                    lines.get(4).append("    |_______|           ");

                } else {
                    lines.getFirst().append(id).append(") _______            ");
                    String mid1, mid2, mid3;

                    switch (c) {
                        case Cannon cannon -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], cannon.getIsDouble() ? "DOUBLE" : "CANNON");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], cannon.getIsDouble() ? "CANNON" : " ");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], " ");
                        }
                        case Engine engine -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], engine.getIsDouble() ? "DOUBLE" : "ENGINE");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], engine.getIsDouble() ? "ENGINE" : " ");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], " ");
                        }
                        case Load load -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], load.getIsSpecial() ? "SPECIAL" : "LOAD");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], load.getIsSpecial() ? "LOAD" : String.format("(Cap: %d)", load.getCapacity()));
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], load.getIsSpecial() ? String.format("(Cap: %d)", load.getCapacity()) : " ");
                        }
                        case BatteryStorage battery -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], "BATTERY");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], "STORAGE");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], String.format("(Cap: %d)", battery.getCapacity()));
                        }
                        case LifeSupport ls -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], "LIFE");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], "SUPPORT");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], String.format("(Col: %s)", ls.getColor() == AlienColor.PURPLE ? "🟣" : "🟤"));
                        }
                        case Shield sh -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], "SHIELD");
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], String.format("(Dir: %s)", sh.getDirection()));
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], " ");
                        }
                        default -> {
                            mid1 = String.format("    |   %s   | %-10s", c.getLink()[0], c.getType().toUpperCase());
                            mid2 = String.format("    | %d %c %d | %-10s", c.getLink()[3], c.getType().charAt(0), c.getLink()[1], " ");
                            mid3 = String.format("    |   %s   | %-10s", c.getLink()[2], " ");
                        }
                    }

                    lines.get(1).append(mid1);
                    lines.get(2).append(mid2);
                    lines.get(3).append(mid3);
                    lines.get(4).append("    |_______|           ");
                }

                colCount++;
                if (colCount == numCols || originalIndex == components.size() - 1) {
                    for (StringBuilder line : lines) {
                        System.out.println(line);
                        line.setLength(0); // reset per la prossima riga
                    }
                    colCount = 0;
                }
                i++;
            }else{
                i++;
            }
        }
    }

    /**
     * Handles the event triggered when a component is successfully linked to a specific position
     * on the game board
     * @param event the event containing the linked component, its position on the board
     *              (row and column), and the list of available components.
     */
    @Override
    public void componentLinked(ComponentLinkedEvent event){
        this.showComponentPlaced(event.getComponent(), event.getX(), event.getY());
        this.showAvailableComponent(event.getAvComponents());
        System.out.print("\n>");

    }

    /**
     * Handles the event triggered when a requested component is unavailable.
     * @param event the event containing details about the unavailable component,
     *              including an error message and information on the available components.
     */
    @Override
    public void componentNotAvailable(ComponentNotAvailableError event){
        this.showError(event.getMessage());
        this.showAvailableComponent(this.viewFlow.getAvailableComponents());
        this.showMessage("Please select a component between 0 and " + (this.viewFlow.getAvailableComponents().size()-1)+"\n");
        System.out.print("\n>");
    }

    /**
     * Handles the event triggered when a player selects a component.
     * @param event the event containing details about the component selection,
     *              including the index of the selected component and the nickname
     *              of the player who made the selection.
     */
    @Override
    public void componentSelected (ComponentSelectedEvent event){
        this.showComponentSelected(this.viewFlow.getSelectedComponent());
        this.showComponentOptions();
        System.out.print("\n>");
    }

    /**
     * Handles the event triggered by the input of invalid coordinates.
     * @param event the event containing details about the invalid coordinates error,
     *              including the username of the player who entered invalid coordinates.
     */
    @Override
    public void invalidCoordinates(InvalidCoordinatesError event){
        this.showError(event.getMessage());
        this.askCoordinates();
        System.out.print("\n>");
    }

    /**
     * Displays the list of components that have been kept by the player(s).
     * @param kept the list of components that have been kept. Each component in the list
     *             will have its type displayed.
     */
    @Override
    public void showKeptComponents(ArrayList<Component> kept) {
        if (!kept.isEmpty()) {
            System.out.println("Components kept: ");
            for (Component component : kept) {
                System.out.println("- " + component.getType());
            }
        }
        System.out.print("\n>");
    }

    /**
     * Handles the event triggered when the hourglass cannot be turned due to a game state restriction.
     * @param event the TurnHourglassError event containing details about the hourglass error,
     *              including the associated error message.
     */
    @Override
    public void turnHourGlassError(TurnHourglassError event){
        this.showError(event.getMessage());
        System.out.print("\n>");
    }

    /**
     * Turns the hourglass with a specified duration, starting a timer for the given number of seconds.
     * @param durationSeconds the duration of the timer in seconds during which the hourglass is turned
     */
    @Override
    public void turnHourGlass(int durationSeconds){
        System.out.println("Timer started! Duration: " + durationSeconds + " seconds.");
        System.out.print("\n>");
    }

    /**
     * Notifies the user upon completion of the building phase, displaying appropriate messages
     * and instructions based on the current game level.
     * @param event the event indicating the completion of the building phase, containing
     *              relevant data about the event context.
     */
    @Override
    public void buildingComplete(BuildCompleteEvent event){
        this.showBuildComplete();

        if(viewFlow.getLevel()==2){
            this.showMessage("Type 'alien' to see where you can place aliens\n" +
                    "> ");

        }
        else{
            this.showMessage("Type 'continue' to start flight phase\n" +
                    "> ");
        }
    }

    /**
     * Handles the event triggered when the player(s) has kept a set of components.
     * @param event the event containing details about the kept components,
     *              including the list of components that were kept by the player(s).
     */
    @Override
    public void keptComponent(KeptComponentEvent event){
        this.showAvailableComponent(event.getAvComponents());
        System.out.print("\n>");
    }

    /**
     * Handles the event triggered when a component is successfully turned.
     * @param event the event containing details about the successful turning
     *              of the component, including the specific component involved.
     */
    @Override
    public void componentTurned(TurnedComponentSuccessEvent event){
        this.showComponentSelected(event.getComponent());
        System.out.print("\n>");
    }

    /**
     * Handles the event triggered when a player selects a kept component.
     * @param event the event containing details about the successfully selected kept component.
     */
    @Override
    public void keptSelected(SuccessSelectKept event){
        this.showComponentSelected(viewFlow.getSelectedComponent());
        System.out.print("\n>");
    }

    /**
     * Processes the AlienCabinCoord event to display information about available alien cabins
     * and prompts the user to insert coordinates for adding aliens.
     * @param event the AlienCabinCoord instance containing the coordinates of available cabins.
     */
    @Override
    public void askAlienCabin(AlienCabinCoord event){
        ArrayList<int[]> coord=event.getAlienCabinCoord();

        if(event.getAlienCabinCoord().isEmpty()){
            this.showMessage("you can't place any alien. Please type 'coord' to go on\n");
            System.out.print(">");
        }
        else {
            StringBuilder message= new StringBuilder();
            for(int[] c:coord){
                message.append("in ( ").append(c[0]).append(" , ").append(c[1]).append(" ): ");
                if(c[1]==0){
                    message.append(" purple alien\n");
                }
                else{
                    message.append(" brown alien\n");
                }
            }
            this.showMessage("\nList of cabin available for aliens:\n" + message);
            this.showMessage("Insert coordinates to add aliens (coord <row1> <col1> <color> <row2> <col2> <color>...), otherwise just 'coord'.");
            System.out.print("\n>");
        }
    }

    /**
     * Handles the event triggered when a stack is successfully dropped.
     * @param event the StackDropOk event containing details about the stack drop,
     *              including the associated success message.
     */
    @Override
    public void dropStack(StackDropOk event){
        this.showMessage(event.getMessage());
        System.out.print("\n>");
    }

    /**
     * Refreshes the available components in the user interface.
     */
    @Override
    public void refreshAvailableComponents(){
        // serve per la GUI, non so se si riesce a integrare anche nella tui al massimo lo lasciamo vuoto
    }

    /**
     * Handles the flight scene process for the given flight start event.
     * @param event an instance of FlightStartEvent representing the
     *              initiation of the flight scene process.
     */
    @Override
    public void flightScene(FlightStartEvent event){
        this.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        this.showPlayerShipStatus();
        this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        this.showFlightPhaseStart();
        viewFlow.handleInput("state");
        viewFlow.setParserFlightState(ParserState.DO_NOTHING);
    }

    /**
     * Displays the current condition and provides additional instructions if the user is the leader.
     * @param condition the current condition to be displayed
     * @param isLeader specifies whether the user is the leader; if true, additional instructions are displayed
     */
    @Override
    public void showCurrCondition(Condition condition, boolean isLeader){
        System.out.println("Current condition:" + condition);
        if(isLeader){
            System.out.println("Type warzone to proceed");
            System.out.print(">");
        }
    }

    /**
     * Handles the completion of a card action, processes truckers' status,
     * and displays their positions along with track details
     * @param event The event containing details of the finished card, including
     *              the list of truckers removed and the current order of truckers.
     */
    @Override
    public void cardFinished(CardFinished event){
        for(Trucker trucker: event.getTruckersRemoved()){
            System.out.println(trucker.getNickname() + " died");
        }
        StringBuilder message = new StringBuilder("Truckers' positions order:\n");
        int i = 1;
        for (Trucker t : event.getTruckers()) {
            message.append(i).append(") ").append(t.getNickname()).append("\n");
            i++;
        }
        this.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        System.out.println(message);
        System.out.print(">");
    }

    /**
     * Handles the epidemic event by processing the given EpidemicEvent object.
     * @param event the EpidemicEvent object containing the list of truckers affected by the epidemic,
     *              as well as the truckers who have been removed.
     */
    @Override
    public  void epidemic(EpidemicEvent event){
        System.out.println("Card effect:");
        for(Trucker trucker: event.getTruckersRemoved()){
            System.out.println(trucker.getNickname() + " died");
        }

        StringBuilder message = new StringBuilder("Truckers' positions order:\n");
        int i = 1;
        for (Trucker t : event.getTruckers()) {
            message.append(i).append(") ").append(t.getNickname()).append("\n");
            i++;
        }
        System.out.println(message);
        System.out.print(">");
    }

    /**
     * Executes the "stardust" effect on the provided StarDustEvent, handling
     * truckers and their interactions with the event's consequences.
     * @param event The StarDustEvent instance containing information about
     *              the affected truckers and their outcomes.
     */
    @Override
    public void stardust(StarDustEvent event){
        System.out.println("Card effect:");
        for (Trucker t: event.getTruckers()) {
            System.out.println(t.getNickname() + " has lost " + t.getSpaceShip().countExposedConnectors() + String.format(t.getSpaceShip().countExposedConnectors() == 1 ? " day\n" : " days\n"));
        }
        for(Trucker trucker: event.getTruckersRemoved()){
            System.out.println(trucker.getNickname() + " died");
        }

        StringBuilder message = new StringBuilder("Truckers' positions order:\n");
        int i = 1;
        for (Trucker t : event.getTruckers()) {
            message.append(i).append(") ").append(t.getNickname()).append("\n");
            i++;
        }
        System.out.println(message);
        System.out.print(">");
    }

    /**
     * Displays the appropriate input format instructions based on the given parser state.
     * @param parserState the current state of the parser, determining which input format instructions to display
     */
    @Override
    public void showInputFormat(ParserState parserState) {
        switch (parserState){
            case ACTIVATE_ENGINES -> {
                if(viewFlow.shipHasDoubleEngines()) {
                    System.out.println("Choose if you want to activate the double engines. If yes type the coordinates of the engines then 'battery' and battery storage(s) coordinates . Type 'y, <row1> <col1>, <row2> <col2>,..., battery, <rows1> <cols1> - <numofbatteries>,...' or 'n': ");
                }
            }
            case ACTIVATE_CANNONS -> {
                if(viewFlow.shipHasDoubleCannons()){
                    System.out.println("Choose if you want to activate the double cannons. If yes type the coordinates of the cannons then 'battery' and battery storage(s) coordinates . Type 'y, <row1> <col1>, <row2> <col2>,..., battery, <rows1> <cols1> - <numofbatteries>,...' or 'n': ");
                }
            }
            case ACTIVATE_SHIELD -> {
                if(viewFlow.shipHasShields()){
                    System.out.println("Choose if you want to activate the shield. If yes type the coordinates of the shield and the coordinates of the battery storage. Type 'y, <row1> <col1>, <rowB1> <colB1>' or 'n': ");
                }
            }
            case LAND -> System.out.println("Type 'y' if you want to land, else 'n'.");
            case HUMAN_COORD -> System.out.println("Insert the coordinates of the cabins to remove the number of human indicated in the card description. Type '<row1> <col1> - <number of crew>, <row2> <col2> - <number of crew>,...' : ");
            case PLOT_GOOD ->{
                 if(viewFlow.shipHasLoad()){
                     System.out.println("Choose if you want the good, if yes type the coordinates of the load and the position in the load. Type 'y, <row> <col>, <position>' or 'n': ");
                 }
            }
            case ROLL_DICE -> System.out.println("Type 'roll' to roll the dices when yu're ready.");
            case PLANET_DECISION->System.out.println("Choose the planet you want to fly to. Type '<index>': ");
            case DRAW_CARD ->System.out.println("Type 'draw' when you're ready to draw the next card.");
            case REMOTION_CONFIRMED, HUMAN_REMOVED -> System.out.println("Remotion confirmed. Type 'continue' when you're ready to go on.");
            case REMOVE_BATTERIES -> System.out.println("You have to remove " + viewFlow.getNumOfBatteriesToRemove() + " batteries." + "\n" + "Choose from where you  want to remove batteries. Type '<row1> <col1> - <quantity>,<row2> <col2> - <quantity>...");
            case REMOVE_GOODS -> System.out.println("Choose goods you want to remove. Type '<index1>,<index2>...'.");
            case REARRANGE ->{
                if(viewFlow.shipHasGoods()){
                    System.out.println("Choose if you want to rearrange your goods. If yes, insert coordinates.\n" +
                            "Type 'y, <row1> <col1> - <value1>, <row2> <col2> - <value2>' or 'n'");
                }
            }
            case COSMIC_CREDITS -> System.out.println("Do you want to gain " + viewFlow.getCosmicCreditsToGain() + " cosmic credits and lose " + viewFlow.getDaysLost() + " flight days? Type '<y/n>'.");
            case WARZONE -> System.out.println("Type 'warzone' to start warzone card");
            case ASK_WITHDRAW -> System.out.println("Do you want to withdraw? Type 'y' or 'n'");
            case DISPLAY_BIG_SHOOT -> System.out.println("Type 'continue' when you are ready for the big Shot");
        }
        System.out.print(">");
    }

    /**
     * Displays the result of a dice roll event by determining and printing the appropriate description.
     * @param event the RollDiceResult object that contains details about the
     *              dice roll event, including shoot and meteor information.
     */
    @Override
    public void showResult(RollDiceResult event){
        if(event.getShoot()!=null){
            System.out.println(this.getShootDescription(event.getShoot(),event.getCurrIndex()));
        }
        else if(event.getMeteor()!=null){
            System.out.println(this.getMeteoriteDescription(event.getMeteor(),event.getCurrIndex()));
        }
        System.out.print(">");
    }

    /**
     * Displays a message indicating that the user has died
     */
    @Override
    public void showDead(){
        System.out.println("You died");
    }

    /**
     * Displays a stylized welcome message in the console
     */
    private void showWelcome() {
        System.out.println("""
                                 __        __   _                            _       \s
                                 \\ \\      / /__| | ___ ___  _ __ ___   ___  | |_ ___ \s
                                  \\ \\ /\\ / / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\\s
                                   \\ V  V /  __/ | (_| (_) | | | | | |  __/ | || (_) |
                                    \\_/\\_/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/        \s
                  ____    _    _        _    __  ____   __    _____ ____  _   _  ____ _  _______ ____ \s
                 / ___|  / \\  | |      / \\   \\ \\/ /\\ \\ / /   |_   _|  _ \\| | | |/ ___| |/ / ____|  _ \\\s
                | |  _  / _ \\ | |     / _ \\   \\  /  \\ V /      | | | |_) | | | | |   | ' /|  _| | |_) |
                | |_| |/ ___ \\| |___ / ___ \\  /  \\   | |       | | |  _ <| |_| | |___| . \\| |___|  _ <\s
                 \\____/_/   \\_\\_____/_/   \\_\\/_/\\_\\  |_|       |_| |_| \\_\\\\___/ \\____|_|\\_\\_____|_| \\_\\\s
                                                  by Cranio Creation           \s
               \s""");
    }

    /**
     * Displays the available colors to the user and prompts them to choose a color
     * @param availableColors the set of colors available for selection
     */
    private void showChooseColor(EnumSet<PlayerColors> availableColors) {
        System.out.println("Colours available: " + String.join(", ", availableColors.toString()));
        System.out.println("Insert nickname and choose a colour.");
        System.out.print(">");
    }

    /**
     * Displays a message indicating that the selected color has already been chosen
     */
    private void showColorTaken() {
        System.out.println("Colour already chosen, choose another one.");
    }

    /**
     * Displays a message indicating that the nickname is already taken
     */
    private void showNicknameTaken() {
        System.out.println("Nickname already taken, choose another one.");
    }

    /**
     * Displays the list of players participating in the game.
     * @param players an ArrayList of Strings representing the names of the players
     */
    private void showPlayers(ArrayList<String> players) {
        System.out.println("Players in the game:");
        for(String player : players) {
            System.out.println(player);
        }
    }

    /**
     * Displays the start of the building phase and lists available components for spaceship construction.
     * @param availableComponents A list of components that are available for use during the building phase.
     */
    private void showBuildPhaseStart(ArrayList<Component> availableComponents) {
        System.out.println("\nStart BUILDING phase. Build your spaceship!");
        showAvailableComponent(availableComponents);
    }

    /**
     * Displays the available options for interacting with a component
     */
    private void showComponentOptions() {
        System.out.println("Options: [Link <row> <column> (to link the component in the position row,col of your spaceship)] [Keep] [Discard]");
        System.out.print("Choose your action: ");
    }

    /**
     * Displays the available build options to the user
     */
    @Override
    public void showBuildOptions() {
        System.out.println("Type 'help' if you want to see commands.");
    }

    /**
     * Displays a message indicating the provided component is being discarded
     * @param component the component to be discarded, whose type will be used in the message
     */
    @Override
    public void showDiscardComponent(Component component) {
        System.out.println("Discard component " + component.getType());
    }

    /**
     * Displays information about the placement of a given component at specified coordinates.
     * @param component the component being placed, containing its type information
     * @param x the row coordinate where the component is placed
     * @param y the column coordinate where the component is placed
     */
    public void showComponentPlaced(Component component, int x, int y) {
        System.out.println("Component " + component.getType() + " linked in [" + x + "," + y + "]");
    }

    /**
     * Displays a message indicating that the timer has expired a certain number of times.
     * @param expirationCount the number of times the timer has expired
     */
    @Override
    public void showTimeExpired(int expirationCount) {
        System.out.println("⏰ Timer ended for the " + ordinal(expirationCount) + " time.");
    }

    /**
     * Determines the ordinal representation of an integer (e.g., 1st, 2nd, 3rd).
     * @param number the integer for which the ordinal representation is to be determined
     * @return the ordinal string representation of the input integer
     */
    private String ordinal(int number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return number + "th";
        }
        return switch (number % 10) {
            case 1 -> number + "st";
            case 2 -> number + "nd";
            case 3 -> number + "rd";
            default -> number + "th";
        };
    }

    /**
     * Displays a message to indicate that the user should wait for other players
     */
    @Override
    public void showWaitForOthers() {
        System.out.println("Wait for other players...");
    }

    /**
     * Displays the provided message to the standard output
     * @param message the message to be displayed
     */
    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays a message indicating that the build process has been completed
     */
    public void showBuildComplete() {
        System.out.println("Building completed!");
    }

    /**
     * Displays the start message of the flight phase
     */
    private void showFlightPhaseStart() {
        System.out.println("Start FLIGHT phase. Get ready for space events!");
    }

    /**
     * Displays the detailed information of a card drawn during the game including its name,
     * description, power, meteorites, shoots, planets, penalties, days lost, and other associated details.
     * @param event the event containing all the information about the card drawn and related game data.
     */
    @Override
    public void showCardDrawn(StartCardEvent event) {
        System.out.println("Card drawn: " + event.getCardName().toUpperCase());
        System.out.println("Card description: " + event.getCardDescription());
        if(event.getPower() != -1){
            System.out.println( "Enemy cannon power: "+event.getPower());
        }
        if(event.getMeteorites() != null) {
            StringBuilder message = new StringBuilder("Meteorites arriving:\n");
            int index = 0;
            for (int[] meteor : event.getMeteorites()) {
                message.append(getMeteoriteDescription(meteor, index));
                index++;
            }
            System.out.println(message);
        }
        if(event.getShoots() != null) {
            StringBuilder message = new StringBuilder("Shoots arriving:\n");
            int index = 0;
            for (int[] shoot : event.getShoots()) {
                message.append(getShootDescription(shoot, index));
                index++;
            }
            System.out.println(message);
        }
        if(event.getPlanets() != null) {
            StringBuilder message = new StringBuilder("Planets to visit:\n");
            int i = 0;
            for (List<Goods> list: event.getPlanets()){
                int red = 0, yellow = 0, green = 0, blue = 0;
                message.append("Planets ").append(i).append(": ");
                i++;
                for(Goods g: list){
                    if(g.getValue() == 1) blue++;
                    else if(g.getValue() == 2) green++;
                    else if(g.getValue() == 3) yellow++;
                    else if(g.getValue() == 4) red++;
                }
                message.append(red).append(" red goods, ").append(yellow).append(" yellow goods, ").append(green).append(" green goods, ").append(blue).append(" blue goods");
            }
            System.out.println(message);
        }
        if(event.getPenalties() != null && event.getConditionsOrder() != null) {
            System.out.println("writing penalties to the client");
            StringBuilder message = new StringBuilder("Conditions and penalties to apply:\n");
            for (Condition condition : event.getConditionsOrder()) {
                Effect effect = event.getPenalties().get(condition);
                message.append(condition.toString()).append(" → ").append(effect.toString()).append("\n");
            }
            System.out.println(message);
        }
        if(event.getDays() != -1) {
            System.out.println("Days to lose: "+event.getDays());
        }
        if(event.getAstronautsLost() != -1) {
            System.out.println("Crew members to lose: "+event.getAstronautsLost());
        }
        if(event.getGoodsLost() != -1){
            System.out.println("Goods to lose: "+event.getGoodsLost() );
        }
        if(event.getCredits() != -1){
            System.out.println("Credits to gain: "+event.getCredits());
        }
        if(event.getNumHumans() != -1) {
            System.out.println( "Humans required: "+event.getNumHumans() );
        }
        if(event.getGoods() != null) {
            int red = 0, yellow = 0, green = 0, blue = 0;
            for (Goods g : event.getGoods()) {
                if(g.getValue() == 1) blue++;
                if(g.getValue() == 2) green++;
                if(g.getValue() == 3) yellow++;
                if(g.getValue() == 4) red++;
            }
            System.out.println("Goods to gain: " + red + " 🟥, " + yellow + " 🟨, " + green + " 🟩, " + blue + " 🟦");
        }
        if(event.getBank() != null) {
            System.out.println("Bank deposit: " + event.getBank().getGoodsInBank(1) + " red goods, " + event.getBank().getGoodsInBank(2) + " yellow goods, " + event.getBank().getGoodsInBank(3) + " green goods, " + event.getBank().getGoodsInBank(4) + " blue goods");
        }

        if(event.getConditionsOrder() != null) {
            this.showCurrCondition(event.getConditionsOrder().getFirst(), false);
        }

        if(event.getPlanetsOccupied() != null) {
            this.updatePlanetsOccupied(event.getPlanetsOccupied());
        }

    }

    /**
     * Generates a description for a meteorite based on its size and direction of arrival.
     * @param meteor an array where the first element represents the size of the
     *               meteorite (1 for big, any other value for small) and the
     *               second element represents the direction of arrival:
     *               0 for top, 1 for right side, 2 for bottom, 3 for left side.
     * @param index  the sequential index of the meteorite used in the description.
     * @return a string describing the meteorite's size, index, and direction of arrival.
     */
    private String getMeteoriteDescription(int[] meteor, int index) {
        int size = meteor[0];
        int direction = meteor[1];

        String sizeText = (size == 1) ? "big" : "small";
        String dirText;
        switch (direction) {
            case 0 -> dirText = "from the top";
            case 1 -> dirText = "from the right side";
            case 2 -> dirText = "from the bottom";
            case 3 -> dirText = "from the left side";
            default -> dirText = "from an unknown direction";
        }

        return "Meteorite " + index + " (" + sizeText + ") arriving " + dirText + "\n";
    }

    /**
     * Generates a descriptive string for a specific shoot based on its size and direction.
     * @param shoot an array containing shoot details where shoot[0] represents the size (1 for big, anything else for small)
     *              and shoot[1] represents the direction (0 for top, 1 for right, 2 for bottom, 3 for left).
     * @param index the index of the shoot, used in the description for identification.
     * @return a string describing the shoot's index, size, and direction.
     */
    private String getShootDescription(int[] shoot, int index) {
        int size = shoot[0];
        int direction = shoot[1];

        String sizeText = (size == 1) ? "big" : "small";
        String dirText;
        switch (direction) {
            case 0 -> dirText = "from the top";
            case 1 -> dirText = "from the right side";
            case 2 -> dirText = "from the bottom";
            case 3 -> dirText = "from the left side";
            default -> dirText = "from an unknown direction";
        }

        return "Shoot " + index + " (" + sizeText + ") arriving " + dirText + "\n";
    }

    /**
     * Updates the list of goods to be removed.
     * @param goodsList the list of goods to be considered for removal; each item in the list
     *                  contains details such as color, X-coordinate, and Y-coordinate
     */
    @Override
    public void updateGoodsListToRemove(ArrayList<Goods> goodsList) {
        if (goodsList == null || goodsList.isEmpty()) {
            System.out.println("No goods available to remove.");
            return;
        }

        System.out.println("Goods available for removal:");
        for (int i = 0; i < goodsList.size(); i++) {
            Goods g = goodsList.get(i);
            System.out.println("Good " + i + ": " + g.getColor() + ", " + g.getX() + ", " + g.getY());
        }
    }

    /**
     * Displays the details of the stardust card drawn during the event.
     * @param event the StarDustEvent object containing the card's details such as name and description
     */
    @Override
    public void showStardustCard(StarDustEvent event) {
        System.out.println("Card drawn: " + event.getCardName().toUpperCase());
        System.out.println("Card description: " + event.getCardDescription());
    }

    /**
     * Updates the current good with the specified goods object.
     * @param good the Goods object to be updated, containing the details of the current good
     */
    @Override
    public void updateCurrGood(Goods good) {
        System.out.println("Good to gain: "+ good.getColor());
    }

    /**
     * Displays an epidemic card by printing its name and description to the console.
     * @param event the EpidemicEvent containing the details of the card to be displayed
     */
    @Override
    public void showEpidemicCard(EpidemicEvent event) {
        System.out.println("Card drawn: " + event.getCardName().toUpperCase());
        System.out.println("Card description: " + event.getCardDescription());
    }

    /**
     * Displays a message indicating that a trucker has withdrawn.
     * @param nickname the nickname of the trucker who has withdrawn
     */
    @Override
    public void showTruckerWithdrawn(String nickname) {
        System.out.println(nickname + " withdraw.");
    }

    /**
     * Displays a message indicating that the game has ended
     */
    @Override
    public void showGameEnded() {
        System.out.println("Game ended. Thanks for playing!");
    }

    /**
     * Displays the final scores of the game, listing each player's score and announcing the winner.
     * @param finalScores a map containing players' names as keys and their corresponding scores as values
     */
    @Override
    public void showScores(Map<String, Integer> finalScores) {
        if(finalScores.isEmpty()){
            System.out.println("No scores. The game isn't started");
            return;
        }
        System.out.println("Final scores:");
        finalScores.forEach((name, score) -> System.out.println("- " + name + ": " + score));
        System.out.println("Winner: " + finalScores.keySet().iterator().next() + "!");
    }

    /**
     * Displays an error message on the console.
     * @param message the error message to be displayed
     */
    @Override
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    /**
     * Displays the status of the player's ship by printing various details to the console
     */
    @Override
    public void showPlayerShipStatus() {
        System.out.println("Position: " + viewFlow.getPosition());
        System.out.println("Total batteries: " + viewFlow.getBatteryCount());
        System.out.println("Total crew members: " + viewFlow.getHumanCount());
        System.out.println("Total cannon power (only single): " + viewFlow.getCannonPower());
        System.out.println("Total engine power (only single): " + viewFlow.getEnginePower());

        System.out.println("Goods on the ship:");
        if (viewFlow.getGoodsByColor() != null) {
            for (Map.Entry<String, Integer> entry : viewFlow.getGoodsByColor().entrySet()) {
                switch (entry.getKey()) {
                    case "RED" -> System.out.println("  - " + entry.getValue() + " 🟥");
                    case "YELLOW" -> System.out.println("  - " + entry.getValue() + " 🟨");
                    case "GREEN" -> System.out.println("  - " + entry.getValue() + " 🟩");
                    case "BLUE" -> System.out.println("  - " + entry.getValue() + " 🟦");
                    default -> System.out.println("  - " + entry.getValue() + " " + entry.getKey());
                }
            }
        }

        System.out.println("Cosmic credits: " + viewFlow.getCredits());
        System.out.println("Lost components: " + viewFlow.getLostComponents());

    }

    /**
     * Displays the layout of a ship along with the details of its components in a formatted output.
     * @param nickname the nickname of the ship whose layout is to be shown
     * @param layout a 2D array representing the layout of the ship, where each element represents a specific component
     */
    @Override
    public void showShipLayout(String nickname, Component[][] layout) {
        System.out.println("\nShip layout of " + nickname + ":\n");

        // Calcolo dinamico celle valide
        boolean[][] validCells = new boolean[11][12];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Component c = layout[i][j];
                validCells[i][j] = c == null || !c.getType().equals("Empty Space");
            }
        }

        // Intestazione colonne (sopra)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        for (int i = 5; i < 10; i++) {
            String rowStr = String.format("%2d", i);

            // Riga superiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? " _______ " : "         ");
            }
            System.out.println();

            // Riga link sopra
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[0]);
            }
            System.out.println();

            // Riga centrale con numero riga sinistra
            System.out.print("  " + rowStr + "   ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else if (c == null) System.out.print("|       |");
                else System.out.printf("| %d %c %d |", c.getLink()[3], c.getType().toUpperCase().charAt(0), c.getLink()[1]);
            }
            System.out.println("   " + rowStr);

            // Riga link sotto
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[2]);
            }
            System.out.println();

            // Riga inferiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? "|_______|" : "         ");
            }
            System.out.println();
        }

        // Intestazione colonne (sotto)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        // Dettagli componenti
        System.out.println("\nComponents details:");
        for (int i = 5; i < 10; i++) {
            for (int j = 4; j < 11; j++) {
                if (!validCells[i][j]) continue;
                Component c = layout[i][j];
                if (c == null) continue;

                StringBuilder details = new StringBuilder();
                switch (c) {
                    case Engine engine -> {
                        if (engine.getIsDouble()) {
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE ENGINE");
                            if(engine.getIsActive()) {
                                details.append(" is active");
                            } else {
                                details.append(" is inactive");
                            }
                        }
                        else {
                            details.append("[").append(i).append(",").append(j).append("] ENGINE");
                        }
                    }
                    case Cannon cannon -> {
                        if (cannon.getIsDouble()) {
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                            if(cannon.getIsActive()) {
                                details.append(" is active");
                            } else {
                                details.append(" is inactive");
                            }
                        } else {
                            details.append("[").append(i).append(",").append(j).append("] CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                        }
                    }
                    case Load load -> {
                        if (load.getIsSpecial()) {
                            details.append("[").append(i).append(",").append(j).append("] SPECIAL LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                int count = 0;
                                for (Goods g : load.getGoods()) {
                                    count++;
                                    details.append(g.getColor()).append(" {").append(count).append("}");
                                    if (count < load.getGoods().size()) {
                                        details.append(", ");
                                    }
                                }
                            } else {
                                details.append(" is empty");
                            }
                        } else {
                            details.append("[").append(i).append(",").append(j).append("] LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                int count = 0;
                                for (Goods g : load.getGoods()) {
                                    count++;
                                    details.append(g.getColor()).append(" {").append(count).append("}");
                                    if (count < load.getGoods().size()) {
                                        details.append(", ");
                                    }
                                }
                            } else {
                                details.append(" is empty");
                            }
                        }
                    }
                    case Shield shield -> {
                        details.append("[").append(i).append(",").append(j).append("] SHIELD (direction: ").append(shield.getDirection()).append(")");
                        if (shield.getIsActive()) {
                            details.append(" is active");
                        } else  {
                            details.append(" is inactive");
                        }
                    }
                    case BatteryStorage battery -> {
                        details.append("[").append(i).append(",").append(j).append("] BATTERY STORAGE (Capacity: ").append(battery.getCapacity()).append(")");
                        if(battery.getQuantity() != 0) {
                            details.append(" contains ").append(battery.getQuantity()).append(battery.getQuantity() == 1 ? " battery" : " batteries");
                        } else {
                            details.append(" is empty");
                        }
                    }
                    case Cabin cabin -> {
                        details.append("[").append(i).append(",").append(j).append("] CABIN");
                        if(cabin.getIsMain()){
                            System.out.println(cabin.getColor());
                        }
                        if(cabin.getQuantity() != 0){
                            if (cabin.getAlien() != null) {
                                details.append(cabin.getQuantity()).append(" contains 1 ").append(cabin.getAlien().getColor()).append(" alien");
                            } else {
                                details.append(" contains ").append(cabin.getQuantity()).append(cabin.getQuantity() == 1 ? " human" : " humans");
                            }
                        } else {
                            details.append(" is empty");
                        }

                    }
                    case LifeSupport lifeSupport -> details.append("[").append(i).append(",").append(j).append("] ").append("LIFE SUPPORT is ").append(lifeSupport.getColor());
                    default -> details.append("[").append(i).append(",").append(j).append("] ").append(c.getType().toUpperCase());
                }
                System.out.println(details);
            }
        }

    }

    /**
     * Displays the layout of another ship, including detailed information about its components.
     * @param nickname the nickname of the ship owner
     * @param ship of the player
     */
    @Override
    public void showOtherShipLayout(String nickname, SpaceShip ship) {
        System.out.println("\nShip layout of " + nickname + ":\n");

        Component[][] layout = ship.getComponents();

        // Calcolo dinamico celle valide
        boolean[][] validCells = new boolean[11][12];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                Component c = layout[i][j];
                validCells[i][j] = c == null || !c.getType().equals("Empty Space");
            }
        }

        // Intestazione colonne (sopra)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        for (int i = 5; i < 10; i++) {
            String rowStr = String.format("%2d", i);

            // Riga superiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? " _______ " : "         ");
            }
            System.out.println();

            // Riga link sopra
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[0]);
            }
            System.out.println();

            // Riga centrale con numero riga sinistra
            System.out.print("  " + rowStr + "   ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else if (c == null) System.out.print("|       |");
                else System.out.printf("| %d %c %d |", c.getLink()[3], c.getType().toUpperCase().charAt(0), c.getLink()[1]);
            }
            System.out.println("   " + rowStr);

            // Riga link sotto
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                Component c = layout[i][j];
                if (!validCells[i][j]) System.out.print("         ");
                else System.out.printf("|   %s   |", c == null ? " " : c.getLink()[2]);
            }
            System.out.println();

            // Riga inferiore
            System.out.print("       ");
            for (int j = 4; j < 11; j++) {
                System.out.print(validCells[i][j] ? "|_______|" : "         ");
            }
            System.out.println();
        }

        // Intestazione colonne (sotto)
        System.out.print("       ");
        for (int col = 4; col < 11; col++) {
            System.out.printf("   %2d    ", col);
        }
        System.out.println();

        // Dettagli componenti
        System.out.println("\nComponents details:");
        for (int i = 5; i < 10; i++) {
            for (int j = 4; j < 11; j++) {
                if (!validCells[i][j]) continue;
                Component c = layout[i][j];
                if (c == null) continue;

                StringBuilder details = new StringBuilder();
                switch (c) {
                    case Engine engine -> {
                        if (engine.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE ENGINE");
                        else
                            details.append("[").append(i).append(",").append(j).append("] ENGINE");
                    }
                    case Cannon cannon -> {
                        if (cannon.getIsDouble())
                            details.append("[").append(i).append(",").append(j).append("] DOUBLE CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                        else
                            details.append("[").append(i).append(",").append(j).append("] CANNON (Direction: ").append(cannon.getOrientation()).append(")");
                    }
                    case Load load -> {
                        if (load.getIsSpecial()) {
                            details.append("[").append(i).append(",").append(j).append("] SPECIAL LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        } else {
                            details.append("[").append(i).append(",").append(j).append("] LOAD (Capacity: ").append(load.getCapacity()).append(")");
                            if (!load.isEmpty()) {
                                details.append(" contains: ");
                                for (Goods g : load.getGoods()) {
                                    details.append(g.getColor()).append(" ");
                                }
                            } else {
                                details.append(" is empty");
                            }
                        }
                    }
                    case Shield shield -> details.append("[").append(i).append(",").append(j).append("] SHIELD (direction: ").append(shield.getDirection()).append(")");
                    case BatteryStorage battery -> {
                        details.append("[").append(i).append(",").append(j).append("] BATTERY STORAGE (Capacity: ").append(battery.getCapacity()).append(")");
                        if(battery.getQuantity() != 0) {
                            details.append(" contains ").append(battery.getQuantity()).append(battery.getQuantity() == 1 ? " battery" : " batteries");
                        } else {
                            details.append(" is empty");
                        }
                    }
                    case Cabin cabin -> {
                        details.append("[").append(i).append(",").append(j).append("] CABIN");
                        if(cabin.getIsMain()){
                            System.out.println(cabin.getColor());
                        }
                        if(cabin.getQuantity() != 0){
                            if (cabin.getAlien() != null) {
                                details.append(cabin.getQuantity()).append(" contains 1 ").append(cabin.getAlien().getColor()).append(" alien");
                            } else {
                                details.append(" contains ").append(cabin.getQuantity()).append(cabin.getQuantity() == 1 ? " human" : " humans");
                            }
                        } else {
                            details.append(" is empty");
                        }

                    }
                    case LifeSupport lifeSupport -> details.append("[").append(i).append(",").append(j).append("] ").append("LIFE SUPPORT is ").append(lifeSupport.getColor());
                    default -> details.append("[").append(i).append(",").append(j).append("] ").append(c.getType().toUpperCase());
                }
                System.out.println(details);
            }
        }
    }

    /**
     * Prompts the user to insert coordinates in the format "row col"
     */
    public void askCoordinates() {
        System.out.print("Insert coordinates (row col): ");
    }

    /**
     * Updates the status of planets occupied by checking the list of Truckers.
     * @param planetsOccupied the list of Truckers representing planets;
     *                        null indicates a planet is free,
     *                        otherwise it is occupied by the specified Trucker
     */
    @Override
    public void updatePlanetsOccupied(ArrayList<Trucker> planetsOccupied) {
        int index=0;
        for(Trucker planets: planetsOccupied){
            if(planets==null){
                System.out.println("Planets "+index+" is free");
            }
            else{
                System.out.println("Planets "+index+" is occupied by: "+planets.getNickname());
            }
            index++;
        }
    }

    /**
     * Displays the current track of truckers, sorted by their positions.
     * @param truckers A map containing the truckers' names as keys and their positions as values.
     * @param level An integer indicating the current level or stage of the track.
     */
    @Override
    public void showTrack(Map<String, Integer> truckers, int level) {

        System.out.println("\n" + "Current Track: ");
        List<Map.Entry<String, Integer>> sortedTruckers = new ArrayList<>(truckers.entrySet());
        sortedTruckers.sort(Comparator.comparingInt(Map.Entry::getValue));

        int firstPosition = sortedTruckers.getFirst().getValue();

        for (int i = 0; i < sortedTruckers.size(); i++) {
            String name = sortedTruckers.get(i).getKey();
            int position = sortedTruckers.get(i).getValue();

            int rank = i + 1;

            if (i == 0) {
                System.out.println(rank + "° " + name);
            } else {
                int diff = position - firstPosition;
                System.out.println(rank + "° " + name + " (+" + diff + " days)");
            }
        }
    }

    /**
     * Displays a message prompting the user to decide whether to continue a previously unfinished game
     */
    @Override
    public void showChooseResume(){
        System.out.println("An old game isn't finished yet, do you want to continue? (y/n)");
    }

    /**
     * Displays a prompt to the user to insert the number of players and the game level.
     */
    @Override
    public void showInsertNumPlayers() {
        System.out.println("\nInsert the number of players and the game level:");

    }

    /**
     * Displays the contents of the given stack
     * @param stack the stack object containing the cards to be displayed
     */
    @Override
    public void showStack(Stack stack) {
        System.out.println("\nCards in the stack:");

        if (stack == null || stack.getStack().isEmpty()) {
            System.out.println("[ Empty stack ]");
            return;
        }

        for (int i = 0; i < stack.getStack().size(); i++) {
            Card card = stack.getStack().get(i);
            System.out.printf(" [%02d] %-15s (Level %d)%n",
                    i, card.getName(), card.getLevel());
        }
    }

    /**
     * Displays the detailed information about the selected component, including its specific attributes
     * and graphical representation of its links and type.
     * @param c the component to be displayed. The method accepts various types of components such as
     *          Engine, Cannon, Load, Shield, BatteryStorage, LifeSupport, or any other defined components.
     *          If the component is double (for Engine or Cannon), special (for Load), or has other unique
     *          attributes, these will be reflected in the detailed output.
     */
    public void showComponentSelected(Component c) {
        StringBuilder info = new StringBuilder();
        if(c != null){
            switch(c) {
                case Engine engine -> {
                    if(engine.getIsDouble()) {
                        info.append("DOUBLE ");
                    }
                    info.append("ENGINE");
                }
                case Cannon cannon -> {
                    if(cannon.getIsDouble()) {
                        info.append("DOUBLE ");
                    }
                    info.append("CANNON (Dir: ").append(cannon.getOrientation()).append(")");
                }
                case Load load -> {
                    if(load.getIsSpecial()) {
                        info.append("SPECIAL ");
                    }
                    info.append("LOAD (Capacity: ").append(load.getCapacity()).append(")");
                }
                case Shield shield -> info.append("SHIELD (Direction: ").append(shield.getDirection()).append(")");
                case BatteryStorage batteryStorage -> info.append("BATTERY STORAGE (Capacity: ").append(batteryStorage.getCapacity()).append(")");
                case LifeSupport lifeSupport -> info.append("LIFE SUPPORT (Colour: ").append(lifeSupport.getColor()).append(")");
                default -> info.append(c.getType().toUpperCase());
            }
            System.out.println(info);
            System.out.print(" _______ \n");
            System.out.printf("|   %s   |\n", c.getLink()[0]);
            System.out.printf("| %d %c %d |\n", c.getLink()[3], c.getType().charAt(0), c.getLink()[1]);
            System.out.printf("|   %s   |\n", c.getLink()[2]);
            System.out.print("|_______|\n");

        }
    }

    /**
     * Welcomes players to the old scene by displaying their nicknames and colors, and
     * prompts the user to choose a player by typing their nickname and color.
     * @param colors    A list containing the colors associated with each player.
     * @param nicknames A list containing the nicknames of the players.
     */
    @Override
    public void welcomeOldScene(ArrayList<PlayerColors> colors, ArrayList<String> nicknames) {
        System.out.println("old players:\n");
        System.out.println(colors.getFirst());
        System.out.println(nicknames.getFirst());
        for(int i = 0; i < colors.size(); i++) {
            System.out.println("nickname: " + nicknames.get(i) + " color: " + colors.get(i));
        }
        System.out.println("choose a player, type both nickname and color");
    }
}