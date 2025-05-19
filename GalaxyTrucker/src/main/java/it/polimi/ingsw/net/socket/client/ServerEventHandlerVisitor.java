package it.polimi.ingsw.net.socket.client;

import it.polimi.ingsw.net.socket.events.serverevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.buildingevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.AlienCabinCoord;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.flightevents.*;
import it.polimi.ingsw.net.socket.events.serverevents.setupevents.*;
import it.polimi.ingsw.view.*;
import it.polimi.ingsw.view.text.TUI;

import java.util.ArrayList;


public class ServerEventHandlerVisitor implements ServerEventVisitor{
    private final UI ui;
    private final ViewFlow viewFlow;
    public ServerEventHandlerVisitor(ViewFlow viewFlow){
        this.viewFlow = viewFlow;
        ui = new TUI();
    }

    // === PHASE 1: Lobby ===
    @Override
    public void visit(LobbyEvent event) {
        ui.showWaitingForPlayers(event.getTruckers().size());
        ui.showStartCountdown(event.getCountdown());
    }

    @Override
    public void visit(PlayerJoinedEvent event) {
        ui.showPlayerJoined(event.getUsername());
        if(!viewFlow.getNameAlreadySet()){
            viewFlow.setNickname(event.getUsername());
        }

    }

    @Override
    public void visit(NicknameAlreadyInError event) {
        ui.showNicknameTaken();
        ui.showChooseColor(event.getColors());
    }

    @Override
    public void visit(AvailableColorsEvent event) {
        ui.showWelcome();
        System.out.println("I received: "+ event.getEventType());
        ui.showChooseColor(event.getColors());
    }

    @Override
    public void visit(ColorAlreadyInError event) {
        ui.showError(event.getMessage());
        ui.showColorTaken();
        ui.showChooseColor(event.getAvailableColors());
    }

    @Override
    public void visit(ReconnectedUserEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(DisconnectedUserEvent event) {
        ui.showMessage(event.getMessage());
    }

    // === PHASE 2: Setup ===


    @Override
    public void visit(SetupCompleteEvent event) {
        ui.showSetupComplete();
        viewFlow.setViewState(ViewFlow.ViewState.BULDING);
    }

    // === PHASE 3: Costruzione nave ===
    @Override
    public void visit(StartBuildingEvent event) {
        //potrebbe non funzionare come player joined
        if(!viewFlow.getNameAlreadySet()){
            System.out.println("Inside");
            viewFlow.setNickname(event.getNickname());
        }
        ui.showPlayers(event.getPlayers());
        viewFlow.setViewState(ViewFlow.ViewState.BULDING);
        viewFlow.setShip(event.getSpaceShip().getComponents());
        viewFlow.setStacks(event.getStacks());
        viewFlow.fillAvailableComponents(event.getComponents());
        ui.showBuildPhaseStart(event.getComponents());
        ui.showBuildOptions();
        viewFlow.getTimerDisplay().start(event.getSeconds());
    }
    @Override
    public void visit(ComponentLinkedEvent event) {
        viewFlow.getShip()[event.getX()][event.getY()]=event.getComponent();
        viewFlow.deleteSelectedComponent();
        ui.showComponentPlaced(event.getComponent(), event.getX(), event.getY());
        ui.showAvailableComponent(event.getAvComponents());
    }

    @Override
    public void visit(ComponentNotAvailableError event) {
        ui.showError(event.getMessage());
        ui.showAvailableComponent(viewFlow.getAvailableComponents());
        ui.showMessage("Please select a component between 0 and " + (viewFlow.getAvailableComponents().size()-1));
    }

    @Override
    public void visit(ComponentSelectedEvent event) {
        viewFlow.componentAsUncovered(event.getIndex());
        viewFlow.componentAsSelected(event.getIndex());
        if(viewFlow.getNickname().equals(event.getNickname())) {
            viewFlow.setSelectedComponent(event.getIndex(), false);
            ui.showComponentSelected(viewFlow.getSelectedComponent());
            ui.showComponentOptions();
        }
    }

    @Override
    public void visit(DiscardSuccessEvent event) {
        if(viewFlow.getNickname().equals(event.getNickname())){
            ui.showDiscardComponent(viewFlow.getSelectedComponent());
            viewFlow.deleteSelectedComponent();
        }
        viewFlow.componentAsUnselected(event.getIndex());
        ui.showAvailableComponent(viewFlow.getAvailableComponents());
    }

    @Override
    public void visit(CoordRequest event) {
        ui.askCoordinates();
    }

    @Override
    public void visit(InvalidCoordinatesError event) {
        ui.showError(event.getMessage());
        ui.askCoordinates();
    }

    @Override
    public void visit(InvalidPositionError event) {
        ui.showInvalidPlacement(event.getMessage());
        ui.askCoordinates();
    }

    @Override
    public void visit(UpdateBuildingStateEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(TurnHourglassError event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(AvailableComponentsEvent event) {
        ui.showAvailableComponent(event.getComponents());
        ui.showSelectComponent();
    }

    @Override
    public void visit(NotYourTurnError event) {
    }

    @Override
    public void visit(NumberOfPlayersError event) {
    }

    @Override
    public void visit(PlacedComponentRequest event) {

    }

    @Override
    public void visit(TruckerLostCrewEvent event) {
    }

    @Override
    public void visit(HourglassTurnEvent event) {
        viewFlow.getTimerDisplay().start(event.getSeconds());
    }

    @Override
    public void visit(GameCreatedEvent event) {

    }

    @Override
    public void visit(FirstConnectionEvent event) {

    }

    @Override
    public void visit(CoordListRequest event) {

    }

    @Override
    public void visit(AvailableComponentToSelectRequest event) {

    }

    @Override
    public void visit(TimeExpiredEvent event){
        ui.showTimeExpired();
    }

    @Override
    public void visit(BuildCompleteEvent event){


        ui.showMessage("Your updated ship\n");
        ui.showShipLayout(viewFlow.getNickname(), viewFlow.getShip());
        ui.showBuildComplete();

        viewFlow.setViewState(ViewFlow.ViewState.FILL);

        //ui.showMessage("It's time to fill the spaceship\n");
    }


    @Override
    public void visit(KeptComponentEvent event){
        viewFlow.addKeptComponent(event.getKept());
        viewFlow.deleteSelectedComponent();
        ui.showAvailableComponent(event.getAvComponents());
    }

    @Override
    public void visit(BuildOptionsEvent event){
        ui.showBuildOptions();
    }

    @Override
    public void visit(SuccessSelectKept event){
        viewFlow.setSelectedComponent(event.getComponent(), true);
        viewFlow.getSelectedKeptComponent().remove(event.getComponent());
        ui.showComponentSelected(viewFlow.getSelectedComponent());
    }

    @Override
    public void visit(ShowOtherShipResponse event) {
        ui.showShipLayout(event.getNickname(), event.getSpaceship());
    }

    @Override
    public void visit(WaitOthersBuildingEvent event){
        viewFlow.setEnd();
        ui.showWaitForOthers();
    }

    @Override
    public void visit(AlienCabinCoord event) {
        ArrayList<int[]> coord=event.getAlienCabinCoord();
        String message="";
        for(int[] c:coord){
            message+="x: "+c[0]+" y: "+c[1]+"\n";
        }
        ui.showMessage(message);
    }

    // === PHASE 4: Fase di Volo ===
    @Override
    public void visit(SelectComponentEvent event){
        ui.askPlacedComponent();
    }

    @Override
    public void visit(ShowCardEvent event) {

    }

    @Override
    public void visit(UpdateCardStateEvent event) {
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(UpdateTrackEvent event) {
        viewFlow.setTrack(event.getTrack());
    }

    @Override
    public void visit(ShipUpdateEvent event) {
        viewFlow.setBatteryCount(event.numBatteries());
        viewFlow.setCannonPower(event.cannonPower());
        viewFlow.setShip(event.getShip());
        viewFlow.setEnginePower(event.enginePower());
        viewFlow.setPosition(event.truckerPosition());
        viewFlow.setGoodsByColor(event.getGoods());
        viewFlow.setHumanCount(event.numCrew());
    }

    @Override
    public void visit(IndexesRequest event) {
        ui.showGoodsToRemove(event.getGoodsToRemove());
        ui.askGoodsIndexesToRemove();
    }

    @Override
    public void visit(GoodsGainedEvent event){
        ui.showGoodsGained(event.getGoodGained());
        ui.askIndex();
    }

    @Override
    public void visit(OkEvent event){
        ui.showMessage(event.getMessage());
    }

    @Override
    public void visit(FlightStartEvent event){
        viewFlow.setPosition(event.getPosition());
        viewFlow.setBatteryCount(event.getBatteryCount());
        viewFlow.setHumanCount(event.getHumanCount());
        viewFlow.setCannonPower(event.getCannonPower());
        viewFlow.setEnginePower(event.getEnginePower());
        viewFlow.setViewState(ViewFlow.ViewState.FLIGTH);
        ui.showFlightPhaseStart();

    }

    @Override
    public void visit(IntRequest event) {
        ui.askIndex();
    }

    // === PHASE 5: Fine Gioco ===
    @Override
    public void visit(EndGameEvent event) {
        ui.showWinner(event.getWinner());
        ui.showScores(event.getScores());
        ui.showGameEnded();
    }

    @Override
    public void visit(TruckerWithdrawEvent event) {
        ui.showTruckerWithdrawn(event.getTruckerWithdrawNick());
    }

    // === ERRORI e MESSAGGI GENERICI ===
    @Override
    public void visit(GenericErrorInsertedData event) {
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(NoAvailableSpaceError event) {
        ui.showError(event.getMessage());
    }


    @Override
    public void visit(ReturnStackEvent event){
        ui.showStack(event.getStack());
    }

    @Override
    public void visit(BuildingStateError event){
        ui.showError(event.getMessage());
    }

    @Override
    public void visit(GenericErrorEvent event){
        ui.showError(event.getMessage());
    }




    @Override
    public void visit(WaitOthersStartFlightEvent event) {
        viewFlow.setStartFlight();
        ui.showWaitForOthers();
    }

    // === CARD EVENTS ===

    @Override
    public void visit(BooleanDecision event) { //in tutti si inizia con yes or no
        if (event.getNickname().equals(viewFlow.getNickname())){
            switch (event.getEventType()){
                case ROLL_DICE -> {
                    ui.showMessage("Type 'roll' to roll the dices when you're ready.");
                    viewFlow.setParserFlightState(ParserState.ROLL_DICE);
                }
                case ACTIVATE_CANNONS -> {
                    ui.showMessage("Choose if you want to activate the double cannons. If yes type the coordinates of the cannons. Type 'y, <x1> <y1>, <x2> <y2>,...' or 'n': ");
                    viewFlow.setParserFlightState(ParserState.ACTIVATE_CANNONS);
                }
                case ACTIVATE_ENGINES -> {
                    ui.showMessage("Choose if you want to activate the double engines. If yes type the coordinates of the engines. Type 'y, <x1> <y1>, <x2> <y2>,...' or 'n': ");
                    viewFlow.setParserFlightState(ParserState.ACTIVATE_ENGINES);
                }
                case ACTIVATE_SHIELD -> {
                    ui.showMessage("Choose if you want to activate the shield. If yes type the coordinates of the shield. Type 'y <x> <y>' or 'n': ");
                    viewFlow.setParserFlightState(ParserState.ACTIVATE_SHIELD);
                }
            }
        }
        else {
            ui.showMessage(event.getInfoForOthers());
        }

    }

    @Override
    public void visit(CardFinished event) {
       // viewFlow.setParserFlightState(ParserState.CARD_FINISH);
    }

    @Override
    public void visit(EpidemicEvent event) {
        //viewFlow.setParserFlightState(ParserState.EPIDEMIC);
    }

    @Override
    public void visit(ExecutionEvent event) {
        //viewFlow.setParserFlightState(ParserState.EXECUTION);
    }

    @Override
    public void visit(HumanCoordinates event) {
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.HUMAN_COORD);
            ui.showMessage("Insert the coordinates of the cabins to remove the number of human indicated in the card description. Type '<x1> <y1> - <number of crew>, <x2> <y2> - <number of crew>,...' : ");
        } else {
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(PlanetDecision event) {
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.PLANET_DECISION);
            ui.showMessage("Choose the planet you want to fly to. Type '<index>': ");
        } else {
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(PlotGood event) {
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.PLOT_GOOD);
            ui.showMessage("Choose if you want the good, if yes type the coordinates of the load. Type 'y <x> <y>' or 'n': ");
        } else {
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(RollDice event) {
        if (event.getNickname().equals(viewFlow.getNickname())) {
            viewFlow.setParserFlightState(ParserState.ROLL_DICE);
            ui.showMessage("The dices result is " + event.getDiceResult() + "\n" + event.getMessage());
            //chiedere che cosa deve mandate l'utente
        } else {
            ui.showMessage(event.getInfoForOthers());
        }
    }

    @Override
    public void visit(StarDustEvent event) {
        //fare dopo
        //viewFlow.setParserFlightState(ParserState.STARDUST);

    }

    @Override
    public void visit(StartCardEvent event) {
        ui.showCardDrawn(event);
        if (event.getNickname().equals(viewFlow.getNickname())) {
            ui.showMessage(event.getQuestion());
        } else {
            ui.showMessage(event.getInfoForOthers());
        }
    }
}
