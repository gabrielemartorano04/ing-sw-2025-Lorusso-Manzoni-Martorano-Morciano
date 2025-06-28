package it.polimi.ingsw.net.control;

import it.polimi.ingsw.exceptions.GeneralError;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.events.serverevents.*;
import it.polimi.ingsw.net.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.events.serverevents.setupevents.*;
import it.polimi.ingsw.view.*;

import java.util.ArrayList;

/**
 * The ServerEventHandlerVisitor class serves as a visitor for handling various server events
 * by processing and updating UI and view flow states accordingly
 */
public class ServerEventHandlerVisitor implements VirtualView {

    /**
     * Represents the user interface component of the application
     */
    private final UI ui;

    /**
     * Represents the flow or sequence of views being managed within a specific context
     */
    private final ViewFlow viewFlow;

    /**
     * Constructs a ServerEventHandlerVisitor with the specified ViewFlow and UI
     * @param viewFlow the ViewFlow instance used for managing the view transitions
     * @param ui the UI instance used for interacting with the user interface
     */
    public ServerEventHandlerVisitor(ViewFlow viewFlow, UI ui){
        this.viewFlow = viewFlow;
        this.ui = ui;
    }


    // === PHASE 1: Login and game creation or entrance to an old game ===

    /**
     * Notifies that this game is already full
     *
     * @param event error indicating no available space
     */
    @Override
    public void visit(NoAvailableSpaceError event) {
        ui.showError(event.getMessage());
    }

    /**
     * Receives available colors and shows the welcome screen
     *
     * @param event contains available colors for the client
     */
    @Override
    public void visit(AvailableColorsEvent event) {
        viewFlow.setViewState(ViewFlow.ViewState.START);
        ui.welcomeScene(event);
    }

    /**
     * Notifies that the chosen nickname is already taken.
     *
     * @param event error indicating nickname conflict
     */
    @Override
    public void visit(NicknameAlreadyInError event) {
        ui.nickNameAlreadyTaken(event);
    }

    /**
     * Notifies that the chosen color is already taken.
     *
     * @param event error indicating color conflict
     */
    @Override
    public void visit(ColorAlreadyInError event) {
        ui.colorAlreadyTaken(event);
    }

    /**
     * Notifies a new player joined the game.
     *
     * @param event contains the joined player's info
     */
    @Override
    public void visit(PlayerJoinedEvent event) {
        viewFlow.setAlreadyLogged();
        ui.showPlayerJoined(event.getUsername());
        ui.showWaitForOthers();

        if(!viewFlow.getNameAlreadySet()){
            viewFlow.setNickname(event.getUsername());
            viewFlow.setShip(event.getSpaceShip());
        }

    }

    /**
     * Handles the first connection and prompts for player count
     *
     * @param event first connection notification, which means this client is the first and will create the game
     */

    @Override
    public void visit(FirstConnectionEvent event) {
        ui.showInsertNumPlayers();
        viewFlow.setViewState(ViewFlow.ViewState.FIRST);
    }

    /**
     * Resumes a previously started game by restoring the trucker's state,
     * view state, and UI scene based on the saved game state
     *
     * @param event contains game state, player info, and components to resume the game
     */
    @Override
    public void visit(ResumeGameEvent event) {
        Trucker trucker=null;

        if(viewFlow.getNickname()==null) {
            viewFlow.setNickname(event.getNickname());
        }
        boolean isLeader = viewFlow.getNickname().equalsIgnoreCase(event.getTruckers().getFirst().getNickname());

        for(Trucker t: event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
            }
        }

        if(!trucker.getKeptComponents().isEmpty()){
            for(Component c: trucker.getKeptComponents()){
                viewFlow.addKeptComponent(c);
            }
        }

        if(trucker.getSelectedComponent() != null) {
            viewFlow.setSelectedComponent(trucker.getSelectedComponent());
        }

        viewFlow.setParserFlightState(ParserState.STATE);
        viewFlow.setLevel(event.getLevel());
        if(event.getTrack()!=null) {
            viewFlow.setTrack(event.getTrack());
        }

        viewFlow.setPosition(trucker.getPosition());
        viewFlow.setShip(trucker.getSpaceShip());

        switch (event.getGameState()) {
            case FLIGHT -> {
                viewFlow.setViewState(ViewFlow.ViewState.FLIGTH);
                viewFlow.setStartFlightForAll();
                viewFlow.setEndBuildingPhase();
                ui.resumeFlightScene(event.getPlayerNames());

                if(isLeader){
                    viewFlow.setParserFlightState(ParserState.DRAW_CARD);
                    ui.showInputFormat(ParserState.DRAW_CARD);
                }else{
                    viewFlow.setParserFlightState(ParserState.DO_NOTHING);
                }
            }
            case ENDED -> viewFlow.setViewState(ViewFlow.ViewState.END);
            case BUILDING -> {
                viewFlow.setViewState(ViewFlow.ViewState.BULDING);
                viewFlow.fillAvailableComponents(event.getComponents());
                ui.resumeBuildingScene(event.getPlayerNames());
            }
        }
    }

    /**
     * Sets the view to allow the user to choose whether to resume a previous game
     *
     * @param chooseResumeEvent event signaling resume choice is available
     */
    @Override
    public void visit(ChooseResumeEvent chooseResumeEvent) {
        viewFlow.setViewState(ViewFlow.ViewState.CHOOSE_OLD);
        ui.showChooseResume();
    }

    /**
     * Displays the welcome screen of the old game for returning players with available nicknames and colors,
     * only if no nickname is currently set
     *
     * @param availablePlayersEvent event containing available nicknames and colors of the old game
     */
    @Override
    public void visit(AvailablePlayersEvent availablePlayersEvent) {
        if(viewFlow.getNickname()==null) {
            viewFlow.setViewState(ViewFlow.ViewState.START);
        }else return;
        ui.welcomeOldScene(availablePlayersEvent.getColors(), availablePlayersEvent.getNicknames());
    }


    // === PHASE 2: Building ===

    /**
     * Initializes the building phase by setting nickname, ship, level, components,
     * and starting the building scene (with timer if level is 2)
     *
     * @param event event containing player info and components to build with
     */
    @Override
    public void visit(StartBuildingEvent event) {
        if(!viewFlow.getNameAlreadySet()){
            viewFlow.setNickname(event.getNickname());
            viewFlow.setShip(event.getSpaceShip());
        }

        viewFlow.setLevel(event.getLevel());
        viewFlow.setViewState(ViewFlow.ViewState.BULDING);
        viewFlow.fillAvailableComponents(event.getComponents());

        viewFlow.setPlayers(event.getPlayers());

        ui.startBuildingScene(event);

        if(viewFlow.getLevel()==2) {
            viewFlow.getTimerDisplay().start(event.getSeconds());
        }
    }

    /**
     * Updates the UI and the viewFlow after a component has been successfully linked to the ship
     *
     * @param event event containing the linked component and its coordinates
     */
    @Override
    public void visit(ComponentLinkedEvent event) {
        viewFlow.setFirstLinked();
        viewFlow.getShip().getComponents()[event.getX()][event.getY()]=event.getComponent();
        viewFlow.deleteSelectedComponent();

        ui.componentLinked(event);
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showKeptComponents(viewFlow.getKeptComponent());
        ui.showBuildOptions();

    }

    /**
     * Handles the case when the client tries to select a component no longer available
     *
     * @param event error indicating component unavailability
     */
    @Override
    public void visit(ComponentNotAvailableError event) {
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showKeptComponents(viewFlow.getKeptComponent());
        ui.showBuildOptions();
        ui.componentNotAvailable(event);
    }

    /**
     * Updates the view after the selection of the component by this client is a success
     *
     * @param event event confirming the selected component and its index
     */
    @Override
    public void visit(ComponentSelectedEvent event) {
        viewFlow.componentAsUncovered(event.getIndex());
        viewFlow.componentAsSelected(event.getIndex());
        ui.refreshAvailableComponents();
        if(viewFlow.getNickname().equals(event.getNickname())) {
            viewFlow.setSelectedComponent(event.getIndex(), false);
            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
            ui.showKeptComponents(viewFlow.getKeptComponent());
            ui.componentSelected(event);
        }
    }

    /**
     * Handles a successful discard action from the player
     *
     * @param event event indicating which component was discarded
     */
    @Override
    public void visit(DiscardSuccessEvent event) {
        if(viewFlow.getNickname().equals(event.getNickname())){
            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
            ui.showKeptComponents(viewFlow.getKeptComponent());
            ui.showBuildOptions();
            ui.showDiscardComponent(viewFlow.getSelectedComponent());
            viewFlow.deleteSelectedComponent();
            ui.showAvailableComponent(viewFlow.getAvailableComponents());
        }
        viewFlow.componentAsUnselected(event.getIndex());

    }

    /**
     * Handles the case where a player tries to place a component in invalid coordinates
     *
     * @param event error indicating invalid ship coordinates
     */
    @Override
    public void visit(InvalidCoordinatesError event) {
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());

        ui.showKeptComponents(viewFlow.getKeptComponent());
        ui.showBuildOptions();
        ui.invalidCoordinates(event);
    }

    /**
     * Handles an error when a player incorrectly attempts to turn the hourglass.
     *
     * @param event error indicating invalid hourglass usage
     */
    @Override
    public void visit(TurnHourglassError event) {
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showKeptComponents(viewFlow.getKeptComponent());
        ui.showBuildOptions();
        ui.turnHourGlassError(event);
    }

    /**
     * Starts the hourglass timer and updates the build UI accordingly
     *
     * @param event event containing the remaining time in seconds
     */
    @Override
    public void visit(HourglassTurnEvent event) {
        viewFlow.getTimerDisplay().start(event.getSeconds());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showKeptComponents(viewFlow.getKeptComponent());
        ui.showBuildOptions();
    }

    /**
     * Handles the case where the user keeps a component instead of placing it
     *
     * @param event event confirming the kept component
     */
    @Override
    public void visit(KeptComponentEvent event){
        viewFlow.addKeptComponent(event.getKept());
        viewFlow.deleteSelectedComponent();

        ui.showAvailableComponent(event.getAvComponents());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());

        ui.showKeptComponents(viewFlow.getKeptComponent());

        ui.showBuildOptions();

    }

    /**
     * Handles the successful rotation of a component and updates the view accordingly
     *
     * @param event event containing the rotated component
     */
    @Override
    public void visit(TurnedComponentSuccessEvent event) {
        viewFlow.setSelectedComponent(event.getComponent());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showKeptComponents(viewFlow.getKeptComponent());
        ui.showBuildOptions();
        ui.componentTurned(event);
    }

    /**
     * Handles successful selection of a kept component by the player
     *
     * @param event event containing the selected component and its index
     */
    @Override
    public void visit(SuccessSelectKept event){
        viewFlow.setSelectedComponent(event.getComponent(), true);
        viewFlow.removeKept(event.getIndex());

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());

        ui.keptSelected(event);

        ui.showKeptComponents(viewFlow.getKeptComponent());
        ui.showBuildOptions();

    }

    /**
     * Indicates that the player must wait for others to complete building
     * @param event event signaling wait state for others building
     */
    @Override
    public void visit(WaitOthersBuildingEvent event){
        viewFlow.setEndBuildingPhase();
        ui.showWaitForOthers();
    }

    /**
     * Handles by checking the nickname in the event and updating
     * the view flow and UI appropriately
     * @param event containing details of the event to be handled
     */
    @Override
    public void visit(WaitOthersHourglassEvent event){
        if(event.getNickname().equals(viewFlow.getNickname())){
            viewFlow.setEndBuildingPhase();
            ui.showWaitForOthers();
        }
        viewFlow.getTimerDisplay().start(90);
    }

    /**
     * Indicates that the player must wait for others to start flight
     *
     * @param event event signaling wait state for others to start flight
     */
    @Override
    public void visit(WaitOthersStartFlightEvent event) {
        ui.showWaitForOthers();
    }

    /**
     * Requests the player to provide coordinates for the alien cabin if he wants to
     *
     * @param event event containing alien cabin coordinate request
     */
    @Override
    public void visit(AlienCabinCoord event) {
        viewFlow.setViewState(ViewFlow.ViewState.FILL);
        viewFlow.setShip(event.getSpaceShip());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.askAlienCabin(event);
    }

    /**
     * Updates the state and UI with the returned stack information
     *
     * @param event event containing the returned stack details
     */
    @Override
    public void visit(ReturnStackEvent event){
        viewFlow.setHasStack(event.getStack().getIndexOfThis());
        ui.showStack(event.getStack());
    }

    /**
     * Handles successful dropping of a stack and updates state/UI accordingly
     *
     * @param event event indicating the stack drop was successful
     */
    @Override
    public void visit(StackDropOk event) {
        viewFlow.setHasStack(-1);
        ui.dropStack(event);
    }

    /**
     * Handles the end of the building phase, stops the timer, updates the ship,
     * and transitions the view to the filling phase.
     *
     * @param event event containing final ship configurations of all truckers
     */
    @Override
    public void visit(BuildCompleteEvent event){
        viewFlow.getTimerDisplay().stop();
        viewFlow.setViewState(ViewFlow.ViewState.FILL);

        for(Trucker t: event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                viewFlow.setShip(t.getSpaceShip());
                viewFlow.setPosition(t.getPosition());
            }
        }
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.buildingComplete(event);
    }

    /** Handles the imposition to the client of removing a component to make the ship legal*/
    @Override
    public void visit(ShipNotLegal event){
        viewFlow.setShip(event.getShip());
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showRemove();
    }


    // === PHASE 3: Flight ===

    /**
     * Initializes the flight phase by setting position, ship, track, and updating the UI
     *
     * @param event event containing initial flight data
     */
    @Override
    public void visit(FlightStartEvent event){
        viewFlow.setPosition(event.getPosition());
        viewFlow.setShip(event.getSpaceShip());
        viewFlow.setViewState(ViewFlow.ViewState.FLIGTH);

        viewFlow.setTrack(event.getTrack());

        ui.flightScene(event);
    }

    /**
     * Updates the current condition of the warzone card, handles deaths, and shows relevant UI.
     *
     * @param event event containing current flight status and player info
     */
    @Override
    public void visit(CurrentCondition event) {
        boolean isLeader = false;
        handleDeath(event.getTruckersRemoved());

        if(!viewFlow.isOut()){
            updatesFromFlight(event.getTruckers());
        }

        if(event.getTrack()!=null){
            viewFlow.setTrack(event.getTrack());
            if (event.getTrack().containsKey(viewFlow.getNickname())) {
                int position = event.getTrack().get(viewFlow.getNickname());
                viewFlow.setPosition(position);
            }
            ui.showTrack(event.getTrack(), viewFlow.getLevel());
        }


        if(event.getTruckers().getFirst().getNickname().equals(viewFlow.getNickname())){
            viewFlow.setParserFlightState(ParserState.CURRENT_CONDITION);
            isLeader = true;
        }
        else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        ui.showCurrCondition(event.getCondition(), isLeader);
        ui.showInputFormat(viewFlow.getParserFlightState());
    }

    /**
     * Displays the current decision that the player can take, updating flight info and UI accordingly.
     *
     * @param event event containing decision data and player info
     */
    @Override
    public void visit(BooleanDecision event) {

        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        if (event.getNickname().equals(viewFlow.getNickname())){
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(viewFlow.getParserFlightState());
        }
        else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Handles the completion of a single card, updates player state, and prompts input
     *
     * @param event event containing updated player and flight info
     */
    @Override
    public void visit(CardFinished event) {
        handleDeath(event.getTruckersRemoved());

        if(!viewFlow.isOut()){
            updatesFromFlight(event.getTruckersRemoved());
        }

        Trucker trucker=null;
        for(Trucker t:event.getTruckers()){
            if(t.getNickname().equals(viewFlow.getNickname())){
                trucker=t;
                break;
            }
        }

        if(!viewFlow.isOut()){
            viewFlow.setCredits(trucker.getCosmicCredit());
            viewFlow.setPosition(trucker.getPosition());

            viewFlow.setTrack(event.getTrack());
            if (event.getTrack().containsKey(viewFlow.getNickname())) {
                int position = event.getTrack().get(viewFlow.getNickname());
                viewFlow.setPosition(position);
            }
            viewFlow.setShip(trucker.getSpaceShip());

            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
            ui.showPlayerShipStatus();

            ui.cardFinished(event);
            viewFlow.setParserFlightState(ParserState.ASK_WITHDRAW);
            ui.showInputFormat(viewFlow.getParserFlightState());

        }
    }

    /**
     * Handles drawing a new card event, updating parser state and UI accordingly
     *
     * @param event event containing leader info for drawing card
     */
    @Override
    public void visit(DrawNewCardEvent event) {
        if (viewFlow.getNickname().equals(event.getLeaderNickname())) {
            viewFlow.setParserFlightState(ParserState.DRAW_CARD);
            ui.showInputFormat(ParserState.DRAW_CARD);
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.showInfoForOthers());
        }
    }

    /**
     * Handles the effect of an epidemic card by updating track, handling deaths,
     * updating UI, and setting parser state
     *
     * @param event event containing epidemic and flight info
     */
    @Override
    public void visit(EpidemicEvent event) {
        viewFlow.setStartFlightForAll();

        ui.showEpidemicCard(event);

        handleDeath(event.getTruckersRemoved());

        viewFlow.setTrack(event.getTrack());
        if (event.getTrack().containsKey(viewFlow.getNickname())) {
            int position = event.getTrack().get(viewFlow.getNickname());
            viewFlow.setPosition(position);
        }
        if(!viewFlow.isOut()) {
            updatesFromFlight(event.getTruckers());

            if (viewFlow.getNickname().equals(event.getTruckers().getFirst().getNickname())) {
                viewFlow.setParserFlightState(ParserState.DRAW_CARD);
            } else viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        ui.epidemic(event);

        if(!viewFlow.getParserFlightState().equals(ParserState.DO_NOTHING)){
            ui.showInputFormat(viewFlow.getParserFlightState());
        }
    }

    /**
     * Updates flight info and UI for human coordinates input event
     *
     * @param event event containing truckers and input info
     */
    @Override
    public void visit(HumanCoordinates event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.HUMAN_COORD);
            ui.showInputFormat(ParserState.HUMAN_COORD);
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Updates flight info and UI for planet decision input event
     *
     * @param event event containing truckers and input info
     */
    @Override
    public void visit(PlanetDecision event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.PLANET_DECISION);
            ui.updatePlanetsOccupied(event.getPlanetsOccupied());
            ui.showInputFormat(ParserState.PLANET_DECISION);
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Updates flight info and UI for plotting goods input event
     *
     * @param event event containing truckers and good info
     */
    @Override
    public void visit(PlotGood event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.PLOT_GOOD);
            ui.updateCurrGood(event.getGood());
            ui.showInputFormat(ParserState.PLOT_GOOD);
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Updates flight info, shows dice roll results, and manages input states
     *
     * @param event event containing dice roll results and player info
     */
    @Override
    public void visit(RollDiceResult event) {
        updatesFromFlight(event.getTruckers());

        ui.showResult(event);
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(viewFlow.getParserFlightState());
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Handles stardust card effects, updates track, deaths, flight info, and UI.
     *
     * @param event event containing stardust card info and player data
     */
    @Override
    public void visit(StarDustEvent event) {
        viewFlow.setStartFlightForAll();

        ui.showStardustCard(event);
        viewFlow.setTrack(event.getTrack());
        if (event.getTrack().containsKey(viewFlow.getNickname())) {
            int position = event.getTrack().get(viewFlow.getNickname());
            viewFlow.setPosition(position);
        }
        handleDeath(event.getTruckersRemoved());

        if(!viewFlow.isOut()){
            updatesFromFlight(event.getTruckers());
            if (viewFlow.getNickname().equals(event.getTruckers().getFirst().getNickname())) {
                viewFlow.setParserFlightState(ParserState.DRAW_CARD);
                ui.showInputFormat(ParserState.DRAW_CARD);
            } else viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        }

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        ui.stardust(event);

        if(!viewFlow.getParserFlightState().equals(ParserState.DO_NOTHING)){
            ui.showInputFormat(viewFlow.getParserFlightState());
        }

    }

    /**
     * Handles the beginning of a new card, updates state, and prompts input format if player is leader
     *
     * @param event event containing card and leader information
     */
    @Override
    public void visit(StartCardEvent event) {
        viewFlow.setStartFlightForAll();

        ui.showCardDrawn(event);
        viewFlow.decreaseCardsToGo();
        viewFlow.setTrack(event.getTrack());
        if (event.getTrack().containsKey(viewFlow.getNickname())) {
            int position = event.getTrack().get(viewFlow.getNickname());
            viewFlow.setPosition(position);
        }
        ui.showTrack(viewFlow.getTrack(), viewFlow.getLevel());
        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        ui.showNumCardLeft();
        if (event.getFirstPlayerNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        } else {
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Handles a rearrangement action, updating the flight state and prompting input if it's the player's turn
     *
     * @param event event containing rearrangement data and updated truckers
     */
    @Override
    public void visit(RearrangeEvent event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Informs the player of the possibility of gaining cosmic credits by losing flight days, then prompts input
     *
     * @param event event containing credit gain and parser state
     */
    @Override
    public void visit(GainCosmicCredits event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setCosmicCreditsToGain(event.getCosmicCredits());
            viewFlow.setDaysLost(event.getDaysLost());
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Shows an error related to invalid coordinates and prompts retry
     *
     * @param event event containing error message and parser state
     */
    @Override
    public void visit(InvalidCoordinatesEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            ui.showError(event.getMessage());
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Handles an error where the player hasn't selected enough batteries for the action and prompts retry
     *
     * @param event event containing error message and parser state
     */
    @Override
    public void visit(NotEnoughBatterieEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            ui.showError(event.getMessage());
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Handles an error caused by the selection of a not consistent component during a flight
     * action and lets the player try again
     *
     * @param event event containing error message and parser state
     */
    @Override
    public void visit(InvalidComponentsExceptionEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            ui.showError(event.getMessage());
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Handles an error caused by selecting an invalid position of a Load in which insert a
     * good and lets the player try again
     *
     * @param event event containing error message and parser state
     */
    @Override
    public void visit(InvalidPositionExceptionEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            ui.showError(event.getMessage());
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Handles an error when a player tries to land on an occupied planet
     *
     * @param event event containing error message and parser state
     */
    @Override
    public void visit(PlanetNotFreeExceptionEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showError(event.getMessage());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Notifies the player of an invalid good selection and lets the player try again
     *
     * @param event event containing error message and next parser state
     */
    @Override
    public void visit(InvalidGoodChosenEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showError(event.getMessage());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Notifies the player that not enough goods were selected and lets the player try again
     *
     * @param event event containing error message and next parser state
     */
    @Override
    public void visit(NotEnoughGoodsChosenEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showError(event.getMessage());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Notifies the player that the selected number of batteries is incorrect and
     * lets the player try again
     *
     * @param event event containing error message and next parser state
     */
    @Override
    public void visit(WrongNumberOfBatteriesSelectedEvent event) {
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.showError(event.getMessage());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Confirms the remotion of batteries or goods and shows the updated ship layout
     *
     * @param event event containing updated ship state and parser state
     */
    @Override
    public void visit(RemotionConfirmed event) {

        if(event.getNickname().equals(viewFlow.getNickname())) {
            updatesFromFlight(event.getTruckers());
            ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
            ui.showPlayerShipStatus();
            viewFlow.setParserFlightState(event.getParserState());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Notifies the client to select from which components he wants to remove the batteries he needs to remove
     *
     * @param event event containing updated ship state and parser state
     */
    @Override
    public void visit(RemoveBatteries event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            viewFlow.setNumOfBatteriesToRemove(event.getBatteriesToRemove());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Notifies the client to select from which components he wants to remove the goods he needs to remove
     *
     * @param event event containing updated ship state and parser state
     */
    @Override
    public void visit(RemoveGoods event) {
        updatesFromFlight(event.getTruckers());

        ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());
        ui.showPlayerShipStatus();
        if(event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(event.getParserState());
            ui.updateGoodsListToRemove(event.getGoodsList());
            ui.showInputFormat(event.getParserState());
        }else{
            viewFlow.setParserFlightState(ParserState.DO_NOTHING);
            ui.showMessage(event.getInfoForOthers());
        }
    }

    /**
     * Manages the event where a trucker decides whether to withdraw from the game
     *
     * @param event event containing the trucker's choice and nickname
     */
    @Override
    public void visit(TruckerWithdrawEvent event) {
        boolean choice=event.getChoice();
        if(choice){
            if(event.getNickname().equalsIgnoreCase(viewFlow.getNickname())) {
                viewFlow.setParserFlightState(ParserState.DEAD);
                ui.showDead();
            }
            else ui.showTruckerWithdrawn(event.getNickname());
        }
        else{
            if(event.getNickname().equalsIgnoreCase(viewFlow.getNickname())) {
                viewFlow.setParserFlightState(ParserState.DO_NOTHING);
                ui.showWaitForOthers();
            }
        }
    }

    /**
     * handle the possible elimination of this client from the game, updating the state of the view
     * and notifies the death
     *
     * @param truckersRemoved list of truckers eliminated from the game
     */
    private void handleDeath(ArrayList<Trucker> truckersRemoved) {
        for (Trucker trucker : truckersRemoved) {
            if (trucker.getNickname().equalsIgnoreCase(viewFlow.getNickname())) {
                viewFlow.setCredits(trucker.getCosmicCredit());
                viewFlow.setPosition(trucker.getPosition());
                viewFlow.setShip(trucker.getSpaceShip());

                viewFlow.setOut();
                viewFlow.setParserFlightState(ParserState.DEAD);

                ui.showShipLayout(viewFlow.getNickname(),viewFlow.getShip().getComponents());
                ui.showPlayerShipStatus();
                ui.showDead();
                viewFlow.handleInput("dead");

                break;
            }
        }
    }

    /**
     * updates the ship of this client with the ship coming from the server after that it has been modified
     *
     * @param truckers list of all the truckers of the game with their updated ships
     */
    private void updatesFromFlight (ArrayList<Trucker> truckers){
        for(Trucker t:truckers){
            if(t.getNickname().equals(viewFlow.getNickname())){

                if(viewFlow.getCredits()!=t.getCosmicCredit()) {
                    viewFlow.setCredits(t.getCosmicCredit());
                }

                if(t.getSpaceShip().isChanged(viewFlow.getShip())) {

                    viewFlow.setShip(t.getSpaceShip());
                    ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip().getComponents());
                    ui.showPlayerShipStatus();
                }
                break;
            }
        }
    }


    // === USED IN MORE THAN ONE PHASE ===

    /**
     * Displays a generic error message coming from the server to the user
     *
     * @param event event containing the error message
     */
    @Override
    public void visit(GenericErrorEvent event){
        ui.showError(event.getMessage());
    }

    /**
     * Displays the ship layout of another player
     *
     * @param event event containing the nickname and ship to display
     */
    @Override
    public void visit(ShowOtherShipResponse event) {
        ui.showOtherShipLayout(event.getNickname(), event.getSpaceship());
    }


    // === END OF THE GAME ===


    /**
     * Handles the end of the game by displaying final scores and updating state
     *
     * @param event event containing the final score map
     */
    @Override
    public void visit(EndGameEvent event) {
        ui.showScores(event.getScores());
        ui.showGameEnded();
        viewFlow.setParserFlightState(ParserState.DO_NOTHING);
        viewFlow.setViewState(ViewFlow.ViewState.END);
    }


    // === PING RESPONSE ===

    /**
     * responds to the pingEvent to confirm the client is still active
     *
     * @param e pingEvent coming from the server
     */
    @Override
    public void visit(PingEvent e){
        viewFlow.respondToPing();
    }




}
